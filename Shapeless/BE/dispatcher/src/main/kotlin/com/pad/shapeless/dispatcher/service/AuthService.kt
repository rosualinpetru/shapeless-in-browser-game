package com.pad.shapeless.dispatcher.service

import com.pad.shapeless.dispatcher.dto.LoginRequest
import com.pad.shapeless.dispatcher.security.TokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService @Autowired constructor(
    val tokenProvider: TokenProvider,
    val authenticationManager: AuthenticationManager,
) {

    fun authUserToken(loginRequest: LoginRequest): String {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password
            )
        )
        SecurityContextHolder.getContext().authentication = authentication
        return tokenProvider.createToken(authentication)
    }
}