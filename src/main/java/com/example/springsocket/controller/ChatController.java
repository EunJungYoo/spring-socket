package com.example.springsocket.controller;

import com.example.springsocket.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    // SimpMessageSendingOperations : 메시지를 도착지까지 보내는 MessageSendingOperations<Destination>를 스프링 프레임워크에 맞춘 것.
    private final SimpMessageSendingOperations sendingOperations;

    // 1. "/app/chat/message" 로 오는 요청을 처리.
    // 2. ChatRoomController가 받아서 "/topic/chat/room/{roomId}"를 구독하고 있는 클라이언트에게 메시지 전달.
    @MessageMapping("/templates/chat/message")
    public void enter(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
        sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomId(),message);
    }


}
