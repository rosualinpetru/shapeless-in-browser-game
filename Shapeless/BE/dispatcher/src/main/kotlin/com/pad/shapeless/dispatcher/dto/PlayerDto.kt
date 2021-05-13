package com.pad.shapeless.dispatcher.dto

import java.util.*

data class PlayerDto(val id: UUID, val name: String, val imageUrl: String?, val score: Int, val isOwner: Boolean)