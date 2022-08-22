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

    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보.
    private Map<String, ChannelTopic> topics;
    // 채팅방에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    // redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    @PostConstruct // Bean 의존관계 주입 완료 후, chatRoom 초기화.
    private void init(){
        // redis에다가 Hash 형태의 자료 생성
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
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

    // 채팅방 입장
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        // topic이 존재하지 않으면 새로 생성. pub/sub 통신을 위한 리스너를 만듦.
        if(topic == null){
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
        }
    }


    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
