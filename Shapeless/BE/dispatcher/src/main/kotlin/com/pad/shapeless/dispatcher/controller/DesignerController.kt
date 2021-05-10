package com.pad.shapeless.dispatcher.controller

import com.pad.shapeless.dispatcher.service.DesignerService
import com.pad.shapeless.dispatcher.service.GameService
import com.pad.shapeless.shared.dto.*

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class DesignerController @Autowired constructor(
    private val messagingTemplate: SimpMessagingTemplate,
    private val designerService: DesignerService,
    private val gameService: GameService
) {

    @MessageMapping("/dispatcher/enroll")
    fun enrollDesigner(@Payload message: Message<Nothing>, headerAccessor: SimpMessageHeaderAccessor) {
        headerAccessor.sessionAttributes!!["host_address"] = message.from.hostAddress
        designerService.enrollDesigner(message.from)
        logger.info("The designer with ip ${message.from.hostAddress} joined! Current designers: ${designerService.count()}")
    }

    @MessageMapping("/dispatcher/joined")
    fun userJoined(@Payload message: Message<Joined>) = run {
        message.payload?.let {
            try {
                gameService.updateJoinedUser(it)
                messagingTemplate.convertAndSend(
                    "/topic/designer/${message.from.hostAddress}",
                    Message(JoinedAck(it.player, it.game))
                )
            } catch (e: Exception) {
                messagingTemplate.convertAndSend(
                    "/topic/designer/${message.from.hostAddress}",
                    Message(JoinedErr(it.player, it.game, e.message ?: "Dispatcher internal error!"))
                )
            }
        }
    }

    @MessageMapping("/dispatcher/left")
    fun userLeft(@Payload message: Message<Left>) =
        message.payload?.let {
            try {
                gameService.updateLeftUser(it)
                messagingTemplate.convertAndSend(
                    "/topic/designer/${message.from.hostAddress}",
                    Message(LeftAck(it.player, it.game))
                )
            } catch (e: Exception) {
                messagingTemplate.convertAndSend(
                    "/topic/designer/${message.from.hostAddress}",
                    Message(LeftErr(it.player, it.game, e.message ?: "Dispatcher internal error!"))
                )
            }
        }


    companion object {
        private val logger = LoggerFactory.getLogger(DesignerController::class.java)
    }

}
