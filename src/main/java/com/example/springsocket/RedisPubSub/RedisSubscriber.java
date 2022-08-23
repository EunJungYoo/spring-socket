package com.example.springsocket.RedisPubSub;

import com.example.springsocket.model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/*
    Redis 구독 서비스
    메시지가 발행될 때까지 대기하다가 메시지가 발행되면 해당 메시지를 읽어서 처리하는 클래스.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber{

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    // 1. 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받음
    public void sendMessage(String publishMessage) {
        try{
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            // 3. ChatMessage 객체를 구독자에게 send
            messagingTemplate.convertAndSend("/topic/chat/room/" + chatMessage.getRoomId(), chatMessage);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
