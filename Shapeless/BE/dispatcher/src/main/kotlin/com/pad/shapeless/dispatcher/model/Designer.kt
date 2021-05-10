package com.pad.shapeless.dispatcher.model

import java.net.InetAddress

data class Designer(val ip: InetAddress) {
    var games = 0
}