package com.pad.shapeless.dispatcher.service

import com.pad.shapeless.dispatcher.dto.PlayerDto
import com.pad.shapeless.dispatcher.repository.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlayerService @Autowired constructor(private val playerRepository: PlayerRepository) {

    fun getPlayersInGame(id: UUID) = playerRepository.findAllByGame_Id(id).map {
        PlayerDto(
            id = it.user.id,
            name = it.user.name,
            imageUrl = it.user.imageUrl,
            score = it.user.score,
            isOwner = it.user.id == it.game.owner.id
        )
    }
}