package com.pad.shapeless.dispatcher.security.oauth2.user

import com.pad.shapeless.dispatcher.model.AuthProvider

abstract class OAuth2UserInfo(val attributes: Map<String, Any>) {
    abstract val id: String?
    abstract val name: String?
    abstract val email: String?
    abstract val imageUrl: String?
    abstract val authProvider: AuthProvider
}
