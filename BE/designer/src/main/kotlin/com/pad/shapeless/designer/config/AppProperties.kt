package com.pad.shapeless.designer.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class AppProperties {
    val hosts = Hosts

    object Hosts {
        var dispatcher: String = ""
    }
}