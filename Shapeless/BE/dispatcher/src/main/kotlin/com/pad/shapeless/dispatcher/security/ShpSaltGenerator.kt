package com.pad.shapeless.dispatcher.security

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class ShpSaltGenerator {
    @Bean
    fun saltGenerator(): SaltGenerator = SaltGenerator()
}

class SaltGenerator {
    fun generateSalt() = (1..6).map { (('A'..'Z') + ('a'..'z') + ('0'..'9')).random() }.joinToString("")
}