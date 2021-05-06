package com.pad.shapeless.dispatcher.model

import com.fasterxml.jackson.annotation.JsonIgnore
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

){
    @JsonIgnore
    @OneToMany(mappedBy = "joinedRoom", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val players: Set<User> = setOf()
}

