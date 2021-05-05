package com.pad.shapeless.dispatcher.security.oauth2

import com.pad.shapeless.dispatcher.exception.OAuth2AuthenticationProcessingException
import com.pad.shapeless.dispatcher.model.User
import com.pad.shapeless.dispatcher.security.UserPrincipal
import com.pad.shapeless.dispatcher.security.oauth2.user.OAuth2UserInfo
import com.pad.shapeless.dispatcher.security.oauth2.user.OAuth2UserInfoFactory
import com.pad.shapeless.dispatcher.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class ShpOAuth2UserService @Autowired constructor(val userService: UserService) : DefaultOAuth2UserService() {

    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User? {
        val oAuth2User: OAuth2User = super.loadUser(oAuth2UserRequest)
        return try {
            processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val oAuth2UserInfo: OAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            oAuth2UserRequest.clientRegistration.registrationId,
            oAuth2User.attributes
        )

        oAuth2UserInfo.name
            ?: throw OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider")

        val email = oAuth2UserInfo.email
            ?: throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")

        val user = userService.getUserByEmail(email)?.let {
            if (it.authProvider != oAuth2UserInfo.authProvider) {
                throw OAuth2AuthenticationProcessingException(
                    """Looks like you're signed up with a ${it.authProvider.name.toLowerCase().capitalize()} account. 
                    |Please use your ${it.authProvider.name.toLowerCase().capitalize()} account to login.
                """.trimMargin()
                )
            }
            updateExistingUser(it, oAuth2UserInfo)
        } ?: registerNewUser(oAuth2UserInfo)

        return UserPrincipal.ofUser(user, oAuth2User.attributes)
    }

    private fun registerNewUser(oAuth2UserInfo: OAuth2UserInfo): User = userService.registerOAuth2User(oAuth2UserInfo)

    private fun updateExistingUser(existingUser: User, oAuth2UserInfo: OAuth2UserInfo): User = userService.updateOAuth2User(existingUser, oAuth2UserInfo)

}