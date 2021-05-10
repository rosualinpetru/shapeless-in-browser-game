package com.pad.shapeless.dispatcher.repository

import com.pad.shapeless.dispatcher.model.Game
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GameRepository : CrudRepository<Game, UUID>