package com.pad.shapeless.dispatcher.service

import com.pad.shapeless.dispatcher.dto.GameCreationRequest
import com.pad.shapeless.dispatcher.dto.GameCreationResponse
import com.pad.shapeless.dispatcher.dto.GameDto
import com.pad.shapeless.dispatcher.exception.ResourceNotFoundException
import com.pad.shapeless.dispatcher.model.Game
import com.pad.shapeless.dispatcher.model.Player
import com.pad.shapeless.dispatcher.repository.GameRepository
import com.pad.shapeless.dispatcher.repository.PlayerRepository
import com.pad.shapeless.dispatcher.repository.UserRepository
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
    private val playerRepository: PlayerRepository
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


    fun getAllGames(): List<GameDto> {
        gameRepository.findAll()
            .forEach { if (playerRepository.findAllByGame_Id(it.id).isEmpty()) gameRepository.delete(it) }
        return gameRepository.findAll().map {
            GameDto(
                id = it.id,
                designer = it.designer,
                difficulty = it.difficulty,
                maxPlayers = it.maxPlayers,
                name = it.name,
            )
        }
    }


    fun getGameById(id: UUID) = gameRepository.findByIdOrNull(id)?.let {
        GameDto(
            id = it.id,
            designer = it.designer,
            difficulty = it.difficulty,
            maxPlayers = it.maxPlayers,
            name = it.name,
        )
    }


    fun updateJoinedUser(joined: Joined) =
        userRepository.findByIdOrNull(joined.player)?.let { user ->
            gameRepository.findByIdOrNull(joined.game)?.let { game ->
                if (!isPlaying(joined.player))
                    playerRepository.save(Player(user = user, game = game))
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
            }
        } ?: throw ResourceNotFoundException(
            "Game",
            "id",
            left.game
        )


    fun isPlaying(id: UUID): Boolean = playerRepository.findAll().any { it.user.id == id }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GameService::class.java)
    }
}
