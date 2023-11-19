package com.example.rsaapi.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class KeyDTO {
    @NonNull private String keyString;
    @NonNull private List<Byte> keyBytes;
}
