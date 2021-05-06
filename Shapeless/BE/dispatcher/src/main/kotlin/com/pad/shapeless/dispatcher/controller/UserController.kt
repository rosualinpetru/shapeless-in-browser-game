package com.pad.shapeless.dispatcher.controller

import com.pad.shapeless.dispatcher.dto.ApiResponse
import com.pad.shapeless.dispatcher.dto.ImageUpdateRequest
import com.pad.shapeless.dispatcher.dto.LeaderboardEntry
import com.pad.shapeless.dispatcher.exception.ResourceNotFoundException
import com.pad.shapeless.dispatcher.model.User
import com.pad.shapeless.dispatcher.security.CurrentUser
import com.pad.shapeless.dispatcher.security.UserPrincipal
import com.pad.shapeless.dispatcher.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class UserController @Autowired constructor(
    private val userService: UserService
) {
    @GetMapping("/users/current")
    @PreAuthorize("hasRole('USER')")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): User =
        userService.getUserById(userPrincipal.getId()) ?: throw ResourceNotFoundException(
            "User",
            "id",
            userPrincipal.getId()
        )

    @PostMapping("/users/update/imageUrl")
    @PreAuthorize("hasRole('USER')")
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

    @GetMapping("/users/leaderboard")
    @PreAuthorize("hasRole('USER')")
    fun getUsersLeaderboard(): List<LeaderboardEntry> =
        userService.getAllLeaderboardEntries()
}