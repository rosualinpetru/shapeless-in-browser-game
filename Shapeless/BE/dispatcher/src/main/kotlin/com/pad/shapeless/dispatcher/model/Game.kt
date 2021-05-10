package com.pad.shapeless.dispatcher.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "games")
data class Game(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    @Enumerated(EnumType.STRING) val difficulty: GameDifficulty,
    val maxPlayers: Int,
    @OneToOne
    @JoinColumn(name = "games_user_fk")
    val owner: User,
    val designer: String
) {
    @JsonIgnore
    @OneToMany(mappedBy = "game", orphanRemoval = true)
    private val players: Set<Player> = setOf()
}
