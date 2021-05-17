package com.pad.shapeless.dispatcher.service

import com.pad.shapeless.dispatcher.dto.InGamePlayerDto
import com.pad.shapeless.dispatcher.dto.PlayerDto
import com.pad.shapeless.dispatcher.model.*
import com.pad.shapeless.dispatcher.repository.GameRepository
import com.pad.shapeless.dispatcher.repository.PlayerRepository
import com.pad.shapeless.dispatcher.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlayerService @Autowired constructor(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository
) {

    private val combinations =
        Shape.values().flatMap { shape -> Color.values().map { color -> SCCombination(shape, color) } }.toSet()

    fun getPlayersInGame(id: UUID) = playerRepository.findAllByGame_Id(id).map {
        PlayerDto(
            id = it.user.id,
            name = it.user.name,
            imageUrl = it.user.imageUrl,
            score = it.user.score,
            isOwner = it.user.id == it.game.owner.id
        )
    }

    private fun generateUnusedCombination(gameId: UUID): SCCombination {
        val used = playerRepository.findAllByGame_Id(gameId).map { SCCombination(it.shape, it.color) }.toSet()
        return combinations.minus(used).random()
    }

    fun joinUser(user: User, game: Game) {
        val generatedNewCombination = generateUnusedCombination(game.id)
        playerRepository.save(
            Player(
                user = user,
                game = game,
                shape = generatedNewCombination.shape,
                color = generatedNewCombination.color,
                orderNr = playerRepository.findAllByGame_Id(game.id).size + 1,
                lives = when (game.difficulty) {
                    GameDifficulty.EASY -> 4
                    GameDifficulty.MEDIUM -> 3
                    GameDifficulty.HARD -> 2
                }
            )
        )
    }

    fun getInActualGamePlayers(gameId: UUID, myId: UUID): List<InGamePlayerDto> {
        val allInGame = playerRepository.findAllByGame_Id(gameId).sortedBy { it.orderNr }
        val nextSelect = allInGame.minByOrNull { it.countGuess }
        return allInGame.map {
            InGamePlayerDto(
                id = it.user.id,
                name = it.user.name,
                color =
                if (myId == it.user.id)
                    if (!it.isColorKnown) it.color else null
                else
                    if (it.isColorKnown) it.color else null,
                shape =
                if (myId == it.user.id)
                    if (!it.isShapeKnown) it.shape else null
                else
                    if (it.isShapeKnown) it.shape else null,
                isChoosing = nextSelect?.user?.id == it.user.id,
                lives = if (myId == it.user.id) it.lives else null
            )
        }
    }
}