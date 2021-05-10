package com.pad.shapeless.designer

import com.pad.shapeless.designer.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class DesignerApp

fun main(args: Array<String>) {
    runApplication<DesignerApp>(*args)
}