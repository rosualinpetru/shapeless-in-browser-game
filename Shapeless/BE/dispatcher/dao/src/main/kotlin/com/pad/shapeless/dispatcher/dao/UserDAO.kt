package com.pad.shapeless.dispatcher.dao

import com.pad.shapeless.dispatcher.model.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserDAO : CrudRepository<User, UUID>