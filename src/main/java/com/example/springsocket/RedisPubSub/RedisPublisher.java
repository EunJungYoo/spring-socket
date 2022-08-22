package com.example.springsocket.RedisPubSub;

import com.example.springsocket.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/*
    Redis 발행 서비스
    채팅방에 입장해서 메시지를 작성하면 해당 메시지를 Redis Topic에 발행.
 */

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message){
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

}
