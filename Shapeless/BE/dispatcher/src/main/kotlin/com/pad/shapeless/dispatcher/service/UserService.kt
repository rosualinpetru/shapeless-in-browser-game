package com.pad.shapeless.dispatcher.service

import com.pad.shapeless.dispatcher.dto.ImageUpdateRequest
import com.pad.shapeless.dispatcher.dto.SignUpRequest
import com.pad.shapeless.dispatcher.exception.BadRequestException
import com.pad.shapeless.dispatcher.model.AuthProvider
import com.pad.shapeless.dispatcher.model.User
import com.pad.shapeless.dispatcher.repository.UserRepository
import com.pad.shapeless.dispatcher.security.SaltGenerator
import com.pad.shapeless.dispatcher.security.oauth2.user.OAuth2UserInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val saltGenerator: SaltGenerator
) {
    @Transactional
    fun getUserById(id: UUID): User? = userRepository.findByIdOrNull(id)

    @Transactional
    fun getUserByEmail(email: String): User? = userRepository.findByEmail(email)

    @Transactional
    fun updateImageUrl(user: User, imageUpdateRequest: ImageUpdateRequest): User? =
        userRepository.save(user.copy(imageUrl = imageUpdateRequest.imageUrl))

    @Transactional
    fun registerUser(signUpRequest: SignUpRequest): User =
        if (userRepository.existsByEmail(signUpRequest.email))
            throw BadRequestException("Email address already in use.")
        else
            userRepository.save(
                saltGenerator.generateSalt().let {
                    User(
                        name = signUpRequest.name,
                        email = signUpRequest.email,
                        password = passwordEncoder.encode("${signUpRequest.password}$it"),
                        authProvider = AuthProvider.LOCAL,
                        salt = it
                    )
                }
            )

    @Transactional
    fun registerOAuth2User(oAuth2UserInfo: OAuth2UserInfo): User =
        userRepository.save(
            User(
                name = oAuth2UserInfo.name!!,
                email = oAuth2UserInfo.email!!,
                imageUrl = oAuth2UserInfo.imageUrl,
                authProvider = oAuth2UserInfo.authProvider,
                providerId = oAuth2UserInfo.id
            )
        )

    @Transactional
    fun updateOAuth2User(existingUser: User, oAuth2UserInfo: OAuth2UserInfo): User =
        userRepository.save(
            User(
                id = existingUser.id,
                name = oAuth2UserInfo.name!!,
                imageUrl = oAuth2UserInfo.imageUrl,
                authProvider = existingUser.authProvider,
                email = existingUser.email
            )
        )
}