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

            Status status = statusRepository.findById(statusId).orElse(null);
            Integer numOfTasksInStatus = status.getStatusTasks().size();

            if (status == null){ throw new RuntimeException("Status not found");}

            Task task = new Task();
            task.setName(taskName);
            task.setDescription(taskDesc);
            task.setStatus(status);
            task.setPosition(numOfTasksInStatus + 1);
            taskRepository.save(task);
            System.out.println(task);
            return ResponseEntity.ok(task);

        } catch (Exception e){
            System.out.println(e);
            return (ResponseEntity<?>) ResponseEntity.internalServerError();
        }

    }

    @PostMapping("editTaskPosition/{id}/{newStatusId}/{newPosition}")
    public ResponseEntity<ActionStatusResponse> editTaskPosition(@PathVariable Integer id, @PathVariable Integer newStatusId, @PathVariable Integer newPosition){
        Task task = taskRepository.findById(id).orElse(null);

        if (task != null) {
            taskService.moveTask(id, newStatusId, newPosition);
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Success", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        } else {
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Fail", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        }
    }

    @PostMapping("editTask/{id}")
    public ResponseEntity<ActionStatusResponse> editStatusName(@PathVariable Integer id, @RequestParam("newTaskName") String newName,
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
            task.setName(newName);
            task.setDescription(newDesc);
            taskRepository.save(task);
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Success", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        } else {
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Fail", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        }
    }


    @DeleteMapping("deleteTask/{id}")
    public ResponseEntity<ActionStatusResponse> deleteTask(@PathVariable Integer id) {
        if (taskRepository.existsById(id)) {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " not found."));
            Integer taskStatusId = task.getStatus().getId();
            taskRepository.deleteById(id);
            taskService.updateTaskPositions(taskStatusId);
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Success", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        } else {
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Fail", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        }
    }


}
