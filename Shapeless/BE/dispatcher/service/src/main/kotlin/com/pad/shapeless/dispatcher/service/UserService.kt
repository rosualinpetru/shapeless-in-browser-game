package com.pad.shapeless.dispatcher.service

import com.pad.shapeless.dispatcher.dao.UserDAO
import com.pad.shapeless.dispatcher.dto.UserDTO
import com.pad.shapeless.dispatcher.dto.UserData
import com.pad.shapeless.dispatcher.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*

@Service
class UserService @Autowired constructor(private val userDAO: UserDAO) {

    /**
     * As the CrudRepository uses Optional, we need to convert it to a nullable type!
     */
    private fun UserDAO.findByIdOrNull(id: UUID): User? = this.findById(id).orElse(null)

    fun addUser(userDTO: UserDTO) {
        userDAO.save(User(userDTO.name))
    }

    fun getUserById(id: UUID): UserData =
        userDAO.findByIdOrNull(id)?.let { UserData(it.id, it.name) } ?: throw Exception("User not found!")

    fun getAllUsers(): List<UserData> = userDAO.findAll().map { UserData(it.id, it.name) }
}