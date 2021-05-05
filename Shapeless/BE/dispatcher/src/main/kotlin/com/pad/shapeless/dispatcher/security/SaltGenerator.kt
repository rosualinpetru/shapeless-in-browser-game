package com.pad.shapeless.dispatcher.security

class SaltGenerator {
    fun generateSalt() = (1..6).map { (('A'..'Z') + ('a'..'z') + ('0'..'9')).random() }.joinToString("")
}