package com.pad.shapeless.designer.config


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandler
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.WebSocketTransport

@Configuration
class ClientWebSocketSockJsStompConfig @Autowired constructor(appProperties: AppProperties) {

    private val dispatcherUrl = "ws://${appProperties.hosts.dispatcher}/ws"

    @Bean
    fun webSocketStompClient(
        webSocketClient: WebSocketClient,
        stompSessionHandler: StompSessionHandler
    ): StompSession {
        while (true) {
            try {
                Thread.sleep(10000)
                val webSocketStompClient = WebSocketStompClient(webSocketClient)
                webSocketStompClient.messageConverter = MappingJackson2MessageConverter()
                return webSocketStompClient.connect(dispatcherUrl, stompSessionHandler).get()
            } catch (e: Exception) {
                Thread.sleep(3000)
            }
        }
    }

    @Bean
    fun webSocketClient(): WebSocketClient =
        SockJsClient(listOf(WebSocketTransport(StandardWebSocketClient()), RestTemplateXhrTransport()))

    @Bean
    fun stompSessionHandler(): StompSessionHandler = ClientStompSessionHandler()

}