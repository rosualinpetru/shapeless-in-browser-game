package com.pad.shapeless.dispatcher.controller

import com.pad.shapeless.dispatcher.dto.LeaderboardEntry
import com.pad.shapeless.dispatcher.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class LeaderboardController @Autowired constructor(
    private val userService: UserService
){
    @GetMapping("leaderboard/users/all")
    @PreAuthorize("hasRole('USER')")
    fun getLeaderboardUsers(): List<LeaderboardEntry> =
        userService.getAllLeaderboardEntries()
    
}