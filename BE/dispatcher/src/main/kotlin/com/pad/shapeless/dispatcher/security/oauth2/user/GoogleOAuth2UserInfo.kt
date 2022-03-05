package com.pad.shapeless.dispatcher.security.oauth2.user

import com.pad.shapeless.dispatcher.model.AuthProvider

class GoogleOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {
    override val id: String? = attributes["sub"] as String?
    override val name: String? = attributes["name"] as String?
    override val email: String? = attributes["email"] as String?
    override val imageUrl: String? = attributes["picture"] as String?
    override val authProvider: AuthProvider = AuthProvider.GOOGLE
}