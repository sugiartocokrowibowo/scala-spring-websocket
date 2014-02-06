package com.khannedy.scala.spring.websocket

import org.springframework.stereotype.Component
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.{CloseStatus, TextMessage, WebSocketSession}
import scala.collection.mutable.ArrayBuffer

/**
 * @author Eko Khannedy
 */
@Component
class ChatWebSocket extends TextWebSocketHandler {

  var array = ArrayBuffer[WebSocketSession]()

  override def handleTextMessage(session: WebSocketSession, message: TextMessage): Unit = {
    array.foreach(socket => {
      socket.sendMessage(message)
    })
  }

  override def afterConnectionEstablished(session: WebSocketSession): Unit =
    array += session

  override def afterConnectionClosed(session: WebSocketSession, status: CloseStatus): Unit =
    array -= session
}
