package com.pad.shapeless.dispatcher.security

import com.pad.shapeless.dispatcher.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.*

class UserPrincipal private constructor(
    private val id: UUID,
    private val name: String,
    private val email: String,
    private val password: String?,
    private val authorities: Collection<GrantedAuthority>,
    private val attributes: Map<String, Any>
) : OAuth2User, UserDetails {

    companion object {
        fun ofUser(user: User, attributes: Map<String, Any> = mapOf()): UserPrincipal =
            UserPrincipal(
                user.id,
                user.name,
                user.email,
                user.password,
                listOf(SimpleGrantedAuthority("ROLE_USER")),
                attributes
            )
    }

    fun getId(): UUID = id

    override fun getPassword(): String? = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getAttributes(): Map<String, Any> = attributes

    override fun getName(): String = name
}
