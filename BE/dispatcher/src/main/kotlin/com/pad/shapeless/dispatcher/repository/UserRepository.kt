package com.pad.shapeless.dispatcher.repository

import com.pad.shapeless.dispatcher.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    override fun findAll(): List<User>
}