package com.pad.shapeless.dispatcher.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(
    val name: String,
    @Id val id: UUID = UUID.randomUUID()
)