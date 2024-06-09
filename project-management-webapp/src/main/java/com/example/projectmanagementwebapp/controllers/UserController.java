package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.ActionStatusResponse;
import com.example.projectmanagementwebapp.entities.User;
import com.example.projectmanagementwebapp.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ActionStatusResponse> Login(@RequestBody String json) {
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            JsonNode rootNode = requestMapper.readTree(json);

            String login = rootNode.get("name").asText();
            String password = rootNode.get("password").asText();

            User user = userRepository.findByName(login);

            if (user != null && user.getPassword().equals(password)) {
                ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Success", user.getId().toString());
                return ResponseEntity.ok(actionStatusResponse);
            } else {
                ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Fail", user.getId().toString());
                return ResponseEntity.ok(actionStatusResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ActionStatusResponse("Fail", null));
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
    public ResponseEntity<ActionStatusResponse> deleteEntity(@PathVariable Integer id) {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Success", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        } else {
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Fail", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        }
    }
}
