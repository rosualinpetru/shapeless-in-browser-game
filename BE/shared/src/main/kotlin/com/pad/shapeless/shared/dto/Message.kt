package com.pad.shapeless.shared.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.net.InetAddress


data class Message<T : Serializable>(
    @JsonProperty("payload") val payload: T? = null,
    @JsonProperty("from") val from: InetAddress = InetAddress.getLocalHost()
)