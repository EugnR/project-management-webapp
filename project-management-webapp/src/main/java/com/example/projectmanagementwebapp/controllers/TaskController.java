package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.*;
import com.example.projectmanagementwebapp.repositories.StatusRepository;
import com.example.projectmanagementwebapp.repositories.TaskRepository;
import com.example.projectmanagementwebapp.repositories.UserRepository;
import com.example.projectmanagementwebapp.services.TaskService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    TaskService taskService;

    @GetMapping("/getAllTasks")
    public List<Task> getAllTasks(){
        List<Task> taskList = taskRepository.findAll();
        return taskList;
    }
    @DeleteMapping("deleteAllTasks")
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }


    @GetMapping("getTasksByStatusId/{statusId}")
    public ResponseEntity<?> getStatusTasks(@PathVariable Integer statusId){

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
    public ResponseEntity<?> createTask(@RequestBody String json){
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            JsonNode rootNode = requestMapper.readTree(json);

            String taskName = rootNode.get("name").asText();
            String taskDesc = rootNode.get("description").asText();
            Integer statusId = rootNode.get("statusId").asInt();
//            Integer creatorId = rootNode.get("creatorId").asInt();

            Status status = statusRepository.findById(statusId).orElse(null);
            Integer numOfTasksInStatus = status.getStatusTasks().size();

            //ПОСЛЕ ДЕБАГА ВЕРНУТЬ!!!!
//            if (status == null){ throw new RuntimeException("Status not found");}

//            User creator = userRepository.findById(creatorId).orElse(null);

            Task task = new Task();
            task.setName(taskName);
            task.setDescription(taskDesc);
            task.setStatus(status);
            task.setPosition(numOfTasksInStatus + 1);
//            task.setCreator(creator);

            taskRepository.save(task);
            System.out.println(task);
            return ResponseEntity.ok(task);

        } catch (Exception e){
            System.out.println(e);
            return (ResponseEntity<?>) ResponseEntity.internalServerError();
        }

    }

    @PostMapping("editTaskPosition/{id}/{newStatusId}/{newPosition}")
    public ResponseEntity<AuthResponse> editTaskPosition(@PathVariable Integer id, @PathVariable Integer newStatusId, @PathVariable Integer newPosition){
        Task task = taskRepository.findById(id).orElse(null);

        if (task != null) {
//            status.setPosition(newPosition);
            taskService.moveTask(id, newStatusId, newPosition);

            AuthResponse authResponse = new AuthResponse("Success", id.toString());
            return ResponseEntity.ok(authResponse);
        } else {
            AuthResponse authResponse = new AuthResponse("Fail", id.toString());
            return ResponseEntity.ok(authResponse);
        }
    }

    @PostMapping("editTask/{id}")
    public ResponseEntity<AuthResponse> editStatusName(@PathVariable Integer id, @RequestParam("newTaskName") String newName,
                                                       @RequestParam("newTaskDesc") String newDesc){

        // Присвоение значений по умолчанию, если параметры пустые
        if (newName == null || newName.trim().isEmpty()) {
            newName = "Без названия";
        }
        if (newDesc == null || newDesc.trim().isEmpty()) {
            newDesc = "Нет описания";
        }

        Task task = taskRepository.findById(id).orElse(null);

        if (task != null) {
//            status.setPosition(newPosition);
            task.setName(newName);
            task.setDescription(newDesc);
            taskRepository.save(task);
            AuthResponse authResponse = new AuthResponse("Success", id.toString());
            return ResponseEntity.ok(authResponse);
        } else {
            AuthResponse authResponse = new AuthResponse("Fail", id.toString());
            return ResponseEntity.ok(authResponse);
        }
    }


    @DeleteMapping("deleteTask/{id}")
    public ResponseEntity<AuthResponse> deleteTask(@PathVariable Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " not found."));

        if (taskRepository.existsById(id)) {
//            taskRepository.deleteById(id);
            taskService.deleteTask(id);
            taskService.updateTaskPositions(task.getStatus().getId());
            AuthResponse authResponse = new AuthResponse("Success", id.toString());
            return ResponseEntity.ok(authResponse);
        } else {
            AuthResponse authResponse = new AuthResponse("Fail", id.toString());
            return ResponseEntity.ok(authResponse);
        }
    }


}
