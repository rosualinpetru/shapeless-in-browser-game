package com.pad.shapeless.dispatcher.controller

import com.pad.shapeless.dispatcher.dto.ApiResponse
import com.pad.shapeless.dispatcher.dto.AuthResponse
import com.pad.shapeless.dispatcher.dto.LoginRequest
import com.pad.shapeless.dispatcher.dto.SignUpRequest
import com.pad.shapeless.dispatcher.service.AuthService
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
    val authService: AuthService,
    val userService: UserService,
) {

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> =
        try {
            ResponseEntity.status(HttpStatus.OK).body(AuthResponse(authService.authUserToken(loginRequest)))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e)
        }


    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> = try {
        val user = userService.registerUser(signUpRequest)
        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/user/me")
            .buildAndExpand(user.id).toUri()
        ResponseEntity.created(location)
            .body<Any>(ApiResponse(true, "User registered successfully!"))
    } catch (e: Exception) {
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e)
    }

}