package com.pad.shapeless.dispatcher.controller

import com.pad.shapeless.dispatcher.dto.GameCreationRequest
import com.pad.shapeless.dispatcher.dto.IsPlayingDto
import com.pad.shapeless.dispatcher.exception.ResourceNotFoundException
import com.pad.shapeless.dispatcher.security.CurrentUser
import com.pad.shapeless.dispatcher.security.UserPrincipal
import com.pad.shapeless.dispatcher.service.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/api")
class GameController @Autowired constructor(
    private val gameService: GameService,
) {

    @PostMapping("/games")
    @PreAuthorize("hasRole('USER')")
    fun createGame(
        @CurrentUser userPrincipal: UserPrincipal,
        @RequestBody gameCreationRequest: GameCreationRequest
    ): ResponseEntity<*> =
        try {
            gameService.createGame(userPrincipal.getId(), gameCreationRequest).let {
                ResponseEntity.status(HttpStatus.OK).body<Any>(it)
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)
        }


    @GetMapping("/games")
    @PreAuthorize("hasRole('USER')")
    fun getGames(
        @CurrentUser userPrincipal: UserPrincipal,
    ): ResponseEntity<*> = ResponseEntity.status(HttpStatus.OK).body<Any>(gameService.getAllNotStartedGames())

    @GetMapping("/games/{id}")
    @PreAuthorize("hasRole('USER')")
    fun getGame(
        @CurrentUser userPrincipal: UserPrincipal, @PathVariable id: UUID,
    ): ResponseEntity<*> = ResponseEntity.status(HttpStatus.OK).body<Any>(
        gameService.getGameById(id) ?: throw ResourceNotFoundException(
            "Game",
            "id",
            id
        )
    )

    @GetMapping("/users/isPlaying")
    @PreAuthorize("hasRole('USER')")
    fun getIsPlaying(
        @CurrentUser userPrincipal: UserPrincipal,
    ): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.OK)
            .body<Any>(IsPlayingDto(userPrincipal.getId(), gameService.isPlaying(userPrincipal.getId())))

}