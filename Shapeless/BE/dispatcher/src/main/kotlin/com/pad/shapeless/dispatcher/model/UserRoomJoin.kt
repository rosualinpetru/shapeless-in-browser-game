package com.pad.shapeless.dispatcher.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users_rooms_join")
data class UserRoomJoin(
    @Id val user: User,
    val room: Room,
)
