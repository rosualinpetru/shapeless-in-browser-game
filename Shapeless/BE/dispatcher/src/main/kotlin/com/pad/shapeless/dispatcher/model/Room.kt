package com.pad.shapeless.dispatcher.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "rooms")
data class Room (
    @Id val roomId: UUID = UUID.randomUUID(),
    @Enumerated(EnumType.STRING) val difficulty: Difficulty,
    val maxPlayers: Int,
    val name: String,
    @OneToOne
    @JoinColumn(name = "rooms_user_fk")
    val owner: User,

)

