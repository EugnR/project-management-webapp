package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.AuthResponse;
import com.example.projectmanagementwebapp.entities.Status;
import com.example.projectmanagementwebapp.entities.User;
import com.example.projectmanagementwebapp.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

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
    public ResponseEntity<?> register(@RequestBody String json){
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            JsonNode rootNode = requestMapper.readTree(json);

            String userName = rootNode.get("name").asText();
            String userPass = rootNode.get("password").asText();
            String userEmail = rootNode.get("email").asText();

            User user = new User();
            user.setName(userName);
            user.setPassword(userPass);
            user.setEmail(userEmail);
            userRepository.save(user);
            System.out.println(user);
            return ResponseEntity.ok(user);

        } catch (Exception e){
            System.out.println(e);
            return (ResponseEntity<?>) ResponseEntity.internalServerError();
        }

    }


    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        List<User> userList = userRepository.findAll();

        return userList;
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id){
        User user  = userRepository.findById(id).orElse(null);

        if (user != null){
            return ResponseEntity.ok(user);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<AuthResponse> deleteEntity(@PathVariable Integer id) {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            AuthResponse authResponse = new AuthResponse("Success", id.toString());
            return ResponseEntity.ok(authResponse);
        } else {
            AuthResponse authResponse = new AuthResponse("Fail", id.toString());
            return ResponseEntity.ok(authResponse);
        }
    }
}
