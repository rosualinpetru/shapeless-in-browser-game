package com.pad.shapeless.dispatcher.dto

import java.util.*

/**
 * This is used when we provide user data, but not all of it. The model
 * might contain sensitive data.
 */
data class UserData(val id: UUID, val name: String)
