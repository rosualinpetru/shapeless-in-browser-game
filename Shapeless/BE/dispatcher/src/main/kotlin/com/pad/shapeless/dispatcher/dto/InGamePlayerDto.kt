package com.pad.shapeless.dispatcher.dto

import com.pad.shapeless.dispatcher.model.Color
import com.pad.shapeless.dispatcher.model.Shape
import java.util.*

data class InGamePlayerDto(
    val id: UUID,
    val name: String,
    val shape: Shape?,
    val color: Color?,
    val isChoosing: Boolean
)
