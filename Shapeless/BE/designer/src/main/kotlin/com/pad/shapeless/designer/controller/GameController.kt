package com.pad.shapeless.designer.controller

import com.pad.shapeless.shared.dto.Joined
import com.pad.shapeless.shared.dto.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class GameController @Autowired constructor(
    private val session: StompSession
) {

    @MessageMapping("/game/{gameId}/join")
    fun joinUser(
        @DestinationVariable gameId: UUID,
        @Payload message: UUID,
        headerAccessor: SimpMessageHeaderAccessor
    ) {
        headerAccessor.sessionAttributes!!["game_id"] = gameId
        headerAccessor.sessionAttributes!!["user"] = message

        session.send(
            "/app/dispatcher/joined",
            Message(Joined(message, gameId))
        )
    }

}
