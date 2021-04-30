package com.pad.shapeless.dispatcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DispatcherApp

fun main(args: Array<String>) {
    runApplication<DispatcherApp>(*args)
}
