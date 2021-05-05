package com.pad.shapeless.dispatcher.controller

import com.pad.shapeless.dispatcher.dto.ApiResponse
import com.pad.shapeless.dispatcher.dto.ImageUpdateRequest
import com.pad.shapeless.dispatcher.exception.ResourceNotFoundException
import com.pad.shapeless.dispatcher.security.CurrentUser
import com.pad.shapeless.dispatcher.security.UserPrincipal
import com.pad.shapeless.dispatcher.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ImageUpdateControllerAux @Autowired constructor(
    private val userService: UserService
) {
    @PostMapping("/users/update/imageUrl")
    fun updateImageUrl(
        @CurrentUser userPrincipal: UserPrincipal,
        @RequestBody imageUpdateRequest: ImageUpdateRequest
    ): ResponseEntity<*> =
        userService.getUserById(userPrincipal.getId())?.let {
            userService.updateImageUrl(it, imageUpdateRequest)
            ResponseEntity.status(HttpStatus.OK).body<Any>(ApiResponse(true, "Image updated!"))
        } ?: throw ResourceNotFoundException(
            "User",
            "id",
            userPrincipal.getId()
        )

}