package org.springybot.dto;

import lombok.Data;

@Data
public class TgUserDTO {
    
    private Long id;

    private String userId;

    private String firstname;

    private String username;

    private String lastname;
}
