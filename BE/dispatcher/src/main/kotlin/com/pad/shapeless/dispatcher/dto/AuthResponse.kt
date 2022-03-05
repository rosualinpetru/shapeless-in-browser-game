package com.pad.shapeless.dispatcher.dto

data class AuthResponse(val accessToken: String) {
    val tokenType = "Bearer"
}
