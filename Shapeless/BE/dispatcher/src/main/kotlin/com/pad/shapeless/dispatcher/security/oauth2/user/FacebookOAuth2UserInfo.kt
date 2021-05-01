package com.pad.shapeless.dispatcher.security.oauth2.user

import com.pad.shapeless.dispatcher.model.AuthProvider

class FacebookOAuth2UserInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {
    override val id: String? = attributes["id"] as String?
    override val name: String? = attributes["name"] as String?
    override val email: String? = attributes["email"] as String?
    override val imageUrl: String? =
        (attributes["picture"] as Map<*, *>?)?.let { it1 ->
            (it1["data"] as Map<*, *>?)?.let { it2 ->
                it2["url"] as String
            }
        }
    override val authProvider: AuthProvider = AuthProvider.FACEBOOK
}