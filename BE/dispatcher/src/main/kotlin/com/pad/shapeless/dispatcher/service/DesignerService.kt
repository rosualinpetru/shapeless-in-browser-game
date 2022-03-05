package com.pad.shapeless.dispatcher.service

import com.pad.shapeless.dispatcher.exception.FullGameCapacityException
import com.pad.shapeless.dispatcher.model.Designer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.InetAddress

@Service
class DesignerService {
    companion object {
        private val designers = mutableListOf<Designer>()
    }

    fun enrollDesigner(ip: InetAddress) = designers.add(Designer(ip))

    fun dismissDesigner(hostAddress: String) = designers.removeIf { it.ip.hostAddress == hostAddress }

    fun count() = designers.size

    fun assignDesigner(): Designer {
        val designer = designers.minByOrNull { it.games } ?: throw FullGameCapacityException
        designer.games++
        return designer
    }

}