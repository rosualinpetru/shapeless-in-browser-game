package com.pad.shapeless.dispatcher.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class AppProperties {
    val auth = Auth
    val oauth2 = OAuth2

    object Auth {
        var tokenSecret: String = ""
        var tokenExpirationMilliseconds: Long = 0
    }

    object OAuth2 {
        var authorizedRedirectUris: List<String> = ArrayList()
    }
}
