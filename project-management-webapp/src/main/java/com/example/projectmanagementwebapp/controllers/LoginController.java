package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.AuthResponse;
import com.example.projectmanagementwebapp.entities.User;
import com.example.projectmanagementwebapp.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> Login(@RequestBody String json) {
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            JsonNode rootNode = requestMapper.readTree(json);

            String login = rootNode.get("name").asText();
            String password = rootNode.get("password").asText();

            User user = userRepository.findByName(login);

            // Пример логики аутентификации. Замените на вашу логику.
            if (user != null && user.getPassword().equals(password)) {
                AuthResponse authResponse = new AuthResponse("Success", user.getId().toString());
                return ResponseEntity.ok(authResponse);
            } else {
                AuthResponse authResponse = new AuthResponse("Fail", user.getId().toString());
                return ResponseEntity.ok(authResponse);

            }


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Fail", null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> processPost(@RequestBody User user) {
        userRepository.save(user);
        System.out.println(user);
        return ResponseEntity.ok(user);
    }
}


