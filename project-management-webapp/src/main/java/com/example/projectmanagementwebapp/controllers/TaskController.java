package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.*;
import com.example.projectmanagementwebapp.repositories.StatusRepository;
import com.example.projectmanagementwebapp.repositories.TaskRepository;
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
public class TaskController {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/getAllTasks")
    public List<Task> getAllStatuses(){
        List<Task> taskList = taskRepository.findAll();
        return taskList;
    }

    @GetMapping("getTasksByStatusId/{statusId}")
    public ResponseEntity<?> getProjectStatuses(@PathVariable Integer statusId){

        Status status = statusRepository.findById(statusId).orElse(null);
        if (status != null){
            List<Task> taskList = taskRepository.findAllByStatus(status);
            return ResponseEntity.status(HttpStatus.OK).body(taskList);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tasks not found");
        }
    }

    @PostMapping("/createTask")
    public ResponseEntity<?> postProject(@RequestBody String json){
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            JsonNode rootNode = requestMapper.readTree(json);

            String taskName = rootNode.get("name").asText();
            String taskDesc = rootNode.get("description").asText();
            Integer statusId = rootNode.get("statusId").asInt();
            Integer taskPosition = rootNode.get("position").asInt();
            Integer creatorId = rootNode.get("creatorId").asInt();

            Status status = statusRepository.findById(statusId).orElse(null);
            User creator = userRepository.findById(creatorId).orElse(null);

            Task task = new Task();
            task.setName(taskName);
            task.setDescription(taskDesc);
            task.setStatus(status);
            task.setPosition(taskPosition);
            task.setCreator(creator);

            taskRepository.save(task);
            System.out.println(task);
            return ResponseEntity.ok(task);

        } catch (Exception e){
            System.out.println(e);
            return (ResponseEntity<?>) ResponseEntity.internalServerError();
        }

    }

    @DeleteMapping("taskDelete/{id}")
    public ResponseEntity<AuthResponse> deleteEntity(@PathVariable Integer id) {

        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            AuthResponse authResponse = new AuthResponse("Success", id.toString());
            return ResponseEntity.ok(authResponse);
        } else {
            AuthResponse authResponse = new AuthResponse("Fail", id.toString());
            return ResponseEntity.ok(authResponse);
        }
    }


}
