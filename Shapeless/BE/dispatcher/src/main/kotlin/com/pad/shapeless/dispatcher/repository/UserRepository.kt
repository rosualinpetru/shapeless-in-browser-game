package com.pad.shapeless.dispatcher.repository

import com.pad.shapeless.dispatcher.model.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    override fun findAll(): List<User>
}