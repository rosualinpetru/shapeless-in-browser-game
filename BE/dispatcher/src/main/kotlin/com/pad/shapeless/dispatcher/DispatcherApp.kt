package com.pad.shapeless.dispatcher

import com.pad.shapeless.dispatcher.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class DispatcherApp

fun main(args: Array<String>) {
    runApplication<DispatcherApp>(*args)
}
