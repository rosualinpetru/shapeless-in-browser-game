package com.pad.shapeless.dispatcher.service

import com.pad.shapeless.dispatcher.exception.ResourceNotFoundException
import com.pad.shapeless.dispatcher.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*


@Service
class ShpUserDetailsService @Autowired constructor(private val userService: UserService) :
    UserDetailsService {
    
    override fun loadUserByUsername(identifier: String): UserDetails =
        UserPrincipal.ofUser(
            userService.getUserByEmail(identifier) ?: throw UsernameNotFoundException("User not found with email : $identifier")
        )

    
    fun loadUserById(id: UUID): UserDetails =
        UserPrincipal.ofUser(userService.getUserById(id) ?: throw  ResourceNotFoundException("User", "id", id))
}