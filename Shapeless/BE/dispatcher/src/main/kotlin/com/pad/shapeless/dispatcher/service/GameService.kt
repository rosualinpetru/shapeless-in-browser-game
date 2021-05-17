package com.pad.shapeless.dispatcher.service

import com.pad.shapeless.dispatcher.dto.GameCreationRequest
import com.pad.shapeless.dispatcher.dto.GameCreationResponse
import com.pad.shapeless.dispatcher.dto.GameDto
import com.pad.shapeless.dispatcher.exception.ResourceNotFoundException
import com.pad.shapeless.dispatcher.model.Color
import com.pad.shapeless.dispatcher.model.Game
import com.pad.shapeless.dispatcher.model.Shape
import com.pad.shapeless.dispatcher.repository.GameRepository
import com.pad.shapeless.dispatcher.repository.PlayerRepository
import com.pad.shapeless.dispatcher.repository.UserRepository
import com.pad.shapeless.shared.dto.GuessDto
import com.pad.shapeless.shared.dto.Joined
import com.pad.shapeless.shared.dto.Left
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameService @Autowired constructor(
    private val designerService: DesignerService,
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val playerRepository: PlayerRepository,
    private val playerService: PlayerService
) {

    fun createGame(id: UUID, gameCreationRequest: GameCreationRequest): GameCreationResponse {
        val designer = designerService.assignDesigner()
        val user = userRepository.findByIdOrNull(id) ?: throw ResourceNotFoundException(
            "User",
            "id",
            id
        )
        val game = Game(
            owner = user,
            designer = designer.ip.hostAddress,
            difficulty = gameCreationRequest.difficulty,
            maxPlayers = gameCreationRequest.maxPlayers,
            name = gameCreationRequest.name
        )
        gameRepository.save(game)
        logger.debug("New game created ${gameRepository.findByIdOrNull(game.id)} ")
        return GameCreationResponse(designer.ip.hostAddress, game.id)
    }


    fun getAllNotStartedGames(): List<GameDto> =
        gameRepository.findAll().filterNot { it.hasStarted }.map {
            GameDto(
                id = it.id,
                designer = it.designer,
                difficulty = it.difficulty,
                maxPlayers = it.maxPlayers,
                name = it.name,
                playersNumber = playerRepository.findAllByGame_Id(it.id).size,
                ownerName = it.owner.name
            )
        }


    fun getGameById(id: UUID) = gameRepository.findByIdOrNull(id)?.let {
        GameDto(
            id = it.id,
            designer = it.designer,
            difficulty = it.difficulty,
            maxPlayers = it.maxPlayers,
            name = it.name,
            playersNumber = playerRepository.findAllByGame_Id(it.id).size,
            ownerName = it.owner.name
        )
    }


    fun updateJoinedUser(joined: Joined) =
        userRepository.findByIdOrNull(joined.player)?.let { user ->
            gameRepository.findByIdOrNull(joined.game)?.let { game ->
                if (!isPlaying(joined.player))
                    if (playerRepository.findAllByGame_Id(game.id).size < game.maxPlayers)
                        if (!game.hasStarted)
                            playerService.joinUser(user, game)
                        else
                            throw Exception("The game has already started!")
                    else
                        throw Exception("The room is full!")
                else
                    throw Exception("You can only play one game at a time!")
                logger.debug("Joined ${user.id} in game ${game.id}")
            } ?: throw ResourceNotFoundException(
                "Game",
                "id",
                joined.game
            )
        } ?: throw ResourceNotFoundException(
            "User",
            "id",
            joined.player
        )


    fun updateLeftUser(left: Left) =
        gameRepository.findByIdOrNull(left.game)?.let { game ->
            if (!game.hasStarted) {
                playerRepository.findByUser_Id(left.player)?.let { player ->
                    logger.debug("${player.id} is leaving the ${game.id}")
                    if (game.owner.id == player.user.id) {
                        if (playerRepository.findAll().count { it.game.id == game.id } == 1) {
                            gameRepository.delete(game)
                        } else {
                            val remaining = playerRepository.findAll().minus(player)
                            gameRepository.save(game.copy(owner = remaining.first().user))
                            playerRepository.delete(player)
                        }
                    } else {
                        playerRepository.delete(player)
                    }
                } ?: throw ResourceNotFoundException(
                    "Player",
                    "id",
                    left.player
                )
            } else {
                val allInGame = playerRepository.findAllByGame_Id(left.game)
                if (allInGame.isEmpty()) {
                    gameRepository.deleteById(left.game)
                    return@let
                }
                if (allInGame.size == 1) {
                    val player = allInGame.first()
                    val user = player.user
                    userRepository.save(user.copy(score = player.points + user.score))
                    playerRepository.delete(player)
                    return@let
                }

                playerRepository.findByUser_Id(left.player)?.let { playerRepository.delete(it) }

            }
        } ?: throw ResourceNotFoundException(
            "Game",
            "id",
            left.game
        )


    fun isPlaying(id: UUID): Boolean = playerRepository.findAll().any { it.user.id == id }

    fun updateStartGame(id: UUID) =
        gameRepository.findByIdOrNull(id)?.let {
            if (it.maxPlayers == playerRepository.findAllByGame_Id(id).size)
                gameRepository.save(it.copy(hasStarted = true))
            else
                throw Exception("Not enough players!")
        }
            ?: throw Exception("Game not found")

    fun resolveGuess(guessDto: GuessDto, gameId: UUID) {
        val guessedColor = Color.valueOf(guessDto.color)
        val guessedShape = Shape.valueOf(guessDto.shape)
        if (guessDto.guessedId == guessDto.guesserId)
            throw Exception("You cannot guess yourself!")
        playerRepository.findByUser_Id(guessDto.guesserId)?.let inLet@{ me ->
            playerRepository.findByUser_Id(guessDto.guessedId)?.let { opponent ->
                val (_1, _2, _3, _4) = Quadruple(
                    opponent.isShapeKnown,
                    opponent.isColorKnown,
                    (guessedShape == opponent.shape),
                    (guessedColor == opponent.color)
                )
                val newMe = playerRepository.save(me.copy(countGuess = me.countGuess + 1))
                if (_1 && _2) {
                    val user = opponent.user
                    userRepository.save(user.copy(score = opponent.points + user.score))
                    playerRepository.delete(opponent)
                    return@inLet
                }
                if (!_1 && _2 && _3 || _1 && !_2 && _4) {
                    val user = opponent.user
                    userRepository.save(user.copy(score = opponent.points + user.score))
                    playerRepository.delete(opponent)
                    playerRepository.save(newMe.copy(points = newMe.points + 50))
                    return@inLet
                }
                if (!_1 && !_2 && _3 && !_4) {
                    playerRepository.save(newMe.copy(points = newMe.points + 50))
                    playerRepository.save(opponent.copy(isShapeKnown = true))
                    return@inLet
                }
                if (!_1 && !_2 && !_3 && _4) {
                    playerRepository.save(newMe.copy(points = newMe.points + 50))
                    playerRepository.save(opponent.copy(isColorKnown = true))
                    return@inLet
                }
                if (!_1 && !_2 && _3 && _4) {
                    val user = opponent.user
                    userRepository.save(user.copy(score = opponent.points + user.score))
                    playerRepository.delete(opponent)
                    playerRepository.save(newMe.copy(points = newMe.points + 200))
                    return@inLet
                }
                if (newMe.lives == 1)
                    playerRepository.delete(newMe)
                else
                    playerRepository.save(newMe.copy(lives = newMe.lives - 1))
            }
        }
    }

    fun hasGameStarted(id: UUID) = gameRepository.findByIdOrNull(id)?.hasStarted ?: false

    private data class Quadruple<A, B, C, D>(val _1: A, val _2: B, val _3: C, val _4: D)


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GameService::class.java)
    }
}
