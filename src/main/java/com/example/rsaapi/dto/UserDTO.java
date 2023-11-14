package com.example.rsaapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private byte[] publicKey;
    private String accessToken;
    private String id;
    private String login;
    private String password;
    private String[] errors;
}
