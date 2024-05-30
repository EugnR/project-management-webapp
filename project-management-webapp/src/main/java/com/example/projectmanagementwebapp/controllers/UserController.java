package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.AuthResponse;
import com.example.projectmanagementwebapp.entities.Status;
import com.example.projectmanagementwebapp.entities.User;
import com.example.projectmanagementwebapp.repositories.UserRepository;
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

    @DeleteMapping("userDelete/{id}")
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
