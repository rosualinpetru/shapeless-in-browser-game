package com.pad.shapeless.dispatcher.controller

import com.pad.shapeless.dispatcher.exception.ResourceNotFoundException
import com.pad.shapeless.dispatcher.model.User
import com.pad.shapeless.dispatcher.security.CurrentUser
import com.pad.shapeless.dispatcher.security.UserPrincipal
import com.pad.shapeless.dispatcher.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
class UserController @Autowired constructor(
    private val userService: UserService
) {
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): User =
        userService.getUserById(userPrincipal.getId()) ?: throw ResourceNotFoundException(
            "User",
            "id",
            userPrincipal.getId()
        )
}