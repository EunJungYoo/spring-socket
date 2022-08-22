package com.example.springsocket.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom implements Serializable { // redis에 저장되는 객체는 serialize 가능해야 하므로 참조하도록 선언.

    private static final long serialVersionUID = 6494678977089006639L;
    private String roomId;
    private String roomName;

    public static ChatRoom create(String name){
        ChatRoom room = new ChatRoom();
        room.roomId = UUID.randomUUID().toString();
        room.roomName = name;
        return room;
    }


}
