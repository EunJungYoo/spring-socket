package com.example.springsocket.controller;

import com.example.springsocket.RedisPubSub.RedisPublisher;
import com.example.springsocket.model.ChatMessage;
import com.example.springsocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatService chatService;

    // 1. "/app/chat/message" 로 오는 요청을 처리.
    // 2. ChatRoomController가 받아서 "/topic/chat/room/{roomId}"를 구독하고 있는 클라이언트에게 메시지 전달.
    @MessageMapping("/chat/message")
    public void enter(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatService.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
        redisPublisher.publish(chatService.getTopic(message.getRoomId()), message);
    }



    // redis를 쓰지 않을 때는 SimpleMessageSendingOperations를 사용해서 메시지를 도착지까지 전송.
    // redis를 썼을 떄는 WebSocket에 발행된 메시지를 redis로 발행(publish)
}
