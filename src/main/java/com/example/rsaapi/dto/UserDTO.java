package com.example.rsaapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String publicKey;
    private String accessToken;
    private String id;
    private String login;
    private String password;
    private List<String> errors;
}