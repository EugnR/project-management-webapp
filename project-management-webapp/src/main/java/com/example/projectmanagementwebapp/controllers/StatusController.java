package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.AuthResponse;
import com.example.projectmanagementwebapp.entities.Project;
import com.example.projectmanagementwebapp.entities.Status;
import com.example.projectmanagementwebapp.repositories.ProjectRepository;
import com.example.projectmanagementwebapp.repositories.StatusRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StatusController {
    @Autowired
    StatusRepository statusRepository;

    @Autowired
    ProjectRepository projectRepository;

    @GetMapping("/getAllStatuses")
    public List<Status> getAllStatuses(){
        List<Status> statusList = statusRepository.findAll();
        return statusList;
    }

    @GetMapping("getStatusesByProjectId/{projectId}")
    public ResponseEntity<?> getProjectStatuses(@PathVariable Integer projectId){

        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null){
            List<Status> statusList = statusRepository.findAllByProject(project);
            return ResponseEntity.status(HttpStatus.OK).body(statusList);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Statuses not found");
        }

    }

    @PostMapping("/createStatus")
    public ResponseEntity<?> createStatus(@RequestBody String json){
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            JsonNode rootNode = requestMapper.readTree(json);

            String statusName = rootNode.get("name").asText();
            Integer projectId= rootNode.get("projectId").asInt();
            Integer position= rootNode.get("position").asInt();


            Project project = projectRepository.findById(projectId).orElse(null);
            //ПОСЛЕ ДЕБАГА ВЕРНУТЬ!!!!
//            if (project == null){ throw new RuntimeException("Project not found");}

            Status status = new Status();
            status.setName(statusName);
            status.setProject(project);
            status.setPosition(position);
            statusRepository.save(status);
            System.out.println(status);
            return ResponseEntity.ok(status);

        } catch (Exception e){
            System.out.println(e);
            return (ResponseEntity<?>) ResponseEntity.internalServerError();
        }

    }

    @DeleteMapping("deleteStatus/{id}")
    public ResponseEntity<AuthResponse> deleteEntity(@PathVariable Integer id) {

        if (statusRepository.existsById(id)) {
            statusRepository.deleteById(id);
            AuthResponse authResponse = new AuthResponse("Success", id.toString());
            return ResponseEntity.ok(authResponse);
        } else {
            AuthResponse authResponse = new AuthResponse("Fail", id.toString());
            return ResponseEntity.ok(authResponse);
        }
    }



}
