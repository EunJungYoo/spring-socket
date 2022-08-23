package com.example.springsocket.service;

import com.example.springsocket.RedisPubSub.RedisSubscriber;
import com.example.springsocket.model.ChatMessage;
import com.example.springsocket.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j // logging
@RequiredArgsConstructor
public class ChatService {

    // redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    @PostConstruct // Bean 의존관계 주입 완료 후, chatRoom 초기화.
    private void init(){
        // redis에다가 Hash 형태의 자료 생성
        opsHashChatRoom = redisTemplate.opsForHash();
    }

    // 모든 채팅방 불러오기
    public List<ChatRoom> findAllRoom(){
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    // 채팅방 하나 불러오기
    public ChatRoom findById(String roomId){
        return opsHashChatRoom.get(CHAT_ROOMS, roomId);
    }

    // 채팅방 생성
    public ChatRoom createRoom(String name){
        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.create(name);
        // 채팅방을 redis의 opsHashChatRoom에 삽입
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

}
