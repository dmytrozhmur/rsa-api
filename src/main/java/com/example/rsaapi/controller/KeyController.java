package com.example.rsaapi.controller;

import com.example.rsaapi.dto.KeyDTO;
import com.example.rsaapi.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/keys")
public class KeyController {
    @Autowired
    private KeyService keyService;

    @GetMapping("/rsa/getPublic")
    public KeyDTO getPublicRsaKey() {
        return keyService.getRsaKey();
    }
}
