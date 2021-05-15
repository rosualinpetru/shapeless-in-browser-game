package com.pad.shapeless.dispatcher.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "players")
data class Player(
    @OneToOne
    @JoinColumn(name = "players_user_fk")
    val user: User,
    @ManyToOne
    @JoinColumn(name = "players_game_fk")
    val game: Game,
    val points: Int = 0,
    @Enumerated(EnumType.STRING)
    val shape: Shape,
    @Enumerated(EnumType.STRING)
    val color: Color,
    val isShapeKnown: Boolean=false,
    val isColorKnown: Boolean=false,
    val countGuess: Int = 0,
    @Id
    val id: UUID = UUID.randomUUID()
)