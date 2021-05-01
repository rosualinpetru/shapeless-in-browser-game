package com.pad.shapeless.dispatcher.security.oauth2.user

import com.pad.shapeless.dispatcher.exception.OAuth2AuthenticationProcessingException
import com.pad.shapeless.dispatcher.model.AuthProvider

object OAuth2UserInfoFactory {
    fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo =
        try {
            when (AuthProvider.valueOf(registrationId.toUpperCase())) {
                AuthProvider.FACEBOOK -> FacebookOAuth2UserInfo(attributes)
                AuthProvider.GOOGLE -> GoogleOAuth2UserInfo(attributes)
                else -> throw Exception()
            }
        } catch (e: Exception) {
            throw OAuth2AuthenticationProcessingException("Sorry! Login with $registrationId is not supported yet.")
        }
}