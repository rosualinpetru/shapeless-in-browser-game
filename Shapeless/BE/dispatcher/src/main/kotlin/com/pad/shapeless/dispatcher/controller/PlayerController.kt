package com.pad.shapeless.dispatcher.controller

import com.pad.shapeless.dispatcher.security.CurrentUser
import com.pad.shapeless.dispatcher.security.UserPrincipal
import com.pad.shapeless.dispatcher.service.PlayerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api")
class PlayerController constructor(
    private val playerService: PlayerService
) {
    @GetMapping("/players/game/{id}")
    @PreAuthorize("hasRole('USER')")
    fun getPlayers(@PathVariable id: UUID): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.OK).body<Any>(playerService.getPlayersInGame(id))

}