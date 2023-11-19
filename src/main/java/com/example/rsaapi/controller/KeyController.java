package com.example.rsaapi.controller;

import com.example.rsaapi.dto.KeyDTO;
import com.example.rsaapi.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/keys")
@ResponseStatus(HttpStatus.ACCEPTED)
public class KeyController {
    @Autowired
    private KeyService keyService;

    @GetMapping("/rsa/getPublic")
    public KeyDTO getPublicRsaKey() {
        return keyService.getRsaKey();
    }
}
