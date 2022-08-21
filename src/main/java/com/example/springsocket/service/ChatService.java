package com.example.springsocket.service;

import com.example.springsocket.model.ChatMessage;
import com.example.springsocket.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j // logging
@RequiredArgsConstructor
public class ChatService {
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct // Bean 의존관계 주입 완료 후, chatRoom 초기화.
    private void init(){
        chatRooms = new LinkedHashMap<>();
    }

    // 모든 채팅방 불러오기
    public List<ChatRoom> findAllRoom(){
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        return result;
    }

    // 채팅방 하나 불러오기
    public ChatRoom findById(String roomId){
        return chatRooms.get(roomId);
    }

    // 채팅방 생성
    public ChatRoom createRoom(String name){
        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.create(name);
        // 채팅방을 chatRooms에 삽입
        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }




}
