package com.pad.shapeless.dispatcher.repository

import com.pad.shapeless.dispatcher.model.Player
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PlayerRepository : CrudRepository<Player, UUID> {
    fun findByUser_Id(id: UUID): Player?
    fun findAllByGame_Id(id: UUID): List<Player>

}
