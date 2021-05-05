package com.pad.shapeless.dispatcher.security

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class ShpSaltGenerator {
    @Bean
    fun saltGenerator(): SaltGenerator = SaltGenerator()
}