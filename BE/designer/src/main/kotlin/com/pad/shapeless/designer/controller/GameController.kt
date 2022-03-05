package com.pad.shapeless.designer.controller

import com.pad.shapeless.shared.dto.*
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
    private val session: StompSession,
) {

    @MessageMapping("/game/{gameId}/join")
    fun joinUser(
        @DestinationVariable gameId: UUID,
        @Payload message: UUID,
        headerAccessor: SimpMessageHeaderAccessor
    ) {
        headerAccessor.sessionAttributes!!["game_id"] = gameId
        headerAccessor.sessionAttributes!!["user"] = message

        synchronized(session) {
            session.send(
                "/app/dispatcher/joined",
                Message(Joined(message, gameId))
            )
        }
    }

    @MessageMapping("/game/{gameId}/start")
    fun startGame(
        @DestinationVariable gameId: UUID,
        @Payload message: UUID,
        headerAccessor: SimpMessageHeaderAccessor
    ) {
        synchronized(session) {
            session.send(
                "/app/dispatcher/start",
                Message(Start(message, gameId))
            )
        }
    }

    @MessageMapping("/game/{gameId}/guess")
    fun guess(
        @DestinationVariable gameId: UUID,
        @Payload payload: GuessDto,
        headerAccessor: SimpMessageHeaderAccessor
    ) {
        session.send(
            "/app/dispatcher/guess",
            Message(Pair(payload, gameId))
        )
    }

}
