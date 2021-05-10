package com.pad.shapeless.dispatcher.controller

import com.pad.shapeless.dispatcher.dto.*
import com.pad.shapeless.dispatcher.service.UserAuthenticatorService
import com.pad.shapeless.dispatcher.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
    private val userAuthenticatorService: UserAuthenticatorService,
    private val userService: UserService,
) {

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> =
        try {
            ResponseEntity.status(HttpStatus.OK)
                .body(AuthResponse(userAuthenticatorService.authUserToken(loginRequest)))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e)
        }

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> = try {
        val user = userService.registerUser(signUpRequest)
        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/users/current")
            .buildAndExpand(user.id).toUri()
        ResponseEntity.created(location)
            .body<Any>(ApiResponse("User registered successfully!"))
    } catch (e: Exception) {
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e)
    }

}