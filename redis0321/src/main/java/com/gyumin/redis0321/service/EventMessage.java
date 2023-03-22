package com.gyumin.redis0321.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EventMessage {
    private Long timestamp;
    private String message;

    // 게시지가 메시지를 만들 때 사용할 메서드
    public EventMessage(String message) {
        this.timestamp = System.currentTimeMillis();
        this.message = message;
    }

    // 구독자가 읽을 메서드
    @JsonCreator
    public EventMessage(@JsonProperty("timestamp") Long timestamp, @JsonProperty("message") String message) {
        this.timestamp = timestamp;
        this.message = message;
    }
}
