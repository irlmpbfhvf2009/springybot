package com.lwdevelop.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    // 傳送者
    private String sender;

    // 接收者
    private String receiver;

    private String ip;

    private String content;

    private Date timestamp;

    private Boolean isUser;

    private Boolean isOnline;



}
