package com.pad.shapeless.dispatcher.security

import com.pad.shapeless.dispatcher.exception.ResourceNotFoundException
import com.pad.shapeless.dispatcher.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class ShpUserDetailsService @Autowired constructor(private val userService: UserService) :
    UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails =
        UserPrincipal.ofUser(
            userService.getUserByEmail(email) ?: throw UsernameNotFoundException("User not found with email : $email")
        )

    @Transactional
    fun loadUserById(id: UUID): UserDetails =
        UserPrincipal.ofUser(userService.getUserById(id) ?: throw  ResourceNotFoundException("User", "id", id))
}