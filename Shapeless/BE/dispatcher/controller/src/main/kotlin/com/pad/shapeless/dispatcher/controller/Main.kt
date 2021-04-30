package com.pad.shapeless.dispatcher.controller

import com.pad.shapeless.dispatcher.dto.UserDTO
import com.pad.shapeless.dispatcher.dto.UserData
import com.pad.shapeless.dispatcher.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class Main @Autowired constructor(private val userService: UserService) {
    @PostMapping("/api/user/add")
    fun addUser(@RequestBody userDTO: UserDTO) = userService.addUser(userDTO)

    @GetMapping("/api/user/get/{id}")
    fun getUser(@PathVariable id: UUID) = userService.getUserById(id)

    @GetMapping("/api/user/list")
    fun allUsers() = userService.getAllUsers()

    @GetMapping("/api/user/get/example")
    fun getUser() = UserDTO("Alin")
}