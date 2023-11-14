package com.example.rsaapi;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;

import static com.example.rsaapi.utils.Encryption.*;

@Slf4j
@SpringBootApplication
public class RsaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RsaApiApplication.class, args);
	}

	@PostConstruct
	public void runTest() {
		KeyPair keyPair = getKeyPair();
		String hello = "Hello, World!";
		byte[] cipherHello = encrypt(hello.getBytes(StandardCharsets.UTF_8), keyPair.getPublic());
		log.info(Base64.getEncoder().encodeToString(cipherHello));
		String plainHello = decrypt(cipherHello, keyPair.getPrivate());
		log.info(plainHello);
	}
}
