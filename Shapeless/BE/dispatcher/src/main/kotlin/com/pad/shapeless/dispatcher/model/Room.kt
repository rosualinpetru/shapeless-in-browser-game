package com.pad.shapeless.dispatcher.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "rooms")
data class Room (
    @Id val roomId: UUID = UUID.randomUUID(),
    val difficulty: Difficulty,
    val owner: User,
)

