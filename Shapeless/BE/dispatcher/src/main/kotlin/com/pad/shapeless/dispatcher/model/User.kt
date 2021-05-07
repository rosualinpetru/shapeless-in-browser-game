package com.pad.shapeless.dispatcher.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "users")
data class User(
    val name: String,
    @Column(unique = true)
    val email: String,

    @JsonIgnore
    val password: String? = null,

    val imageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    val authProvider: AuthProvider,

    @JsonIgnore
    val providerId: String? = null,

    @JsonIgnore
    val salt: String? = null,

    @JsonIgnore
    val isPlaying: Boolean = false,

    val score: Int = 0,

    @Id
    val id: UUID = UUID.randomUUID()
) {
    @JsonIgnore
    @OneToOne(mappedBy = "owner", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val ownedRoom: Room? = null

}