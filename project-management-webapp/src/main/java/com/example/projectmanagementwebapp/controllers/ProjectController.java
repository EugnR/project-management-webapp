package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.AuthResponse;
import com.example.projectmanagementwebapp.entities.Project;
import com.example.projectmanagementwebapp.entities.User;
import com.example.projectmanagementwebapp.repositories.ProjectRepository;
import com.example.projectmanagementwebapp.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/projectGet")
    public List<Project> processGet(@RequestBody String json){
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            JsonNode rootNode = requestMapper.readTree(json);

            Integer userId= rootNode.get("user").asInt();

            User user = userRepository.findById(userId).orElse(null);

            return projectRepository.findAllByUser(user);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }

        //return "0";
    }

    @PostMapping("/projectCreate")
    public ResponseEntity<?> postProject(@RequestBody String json){
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            JsonNode rootNode = requestMapper.readTree(json);

            String projectName = rootNode.get("name").asText();
            Integer userId= rootNode.get("user").asInt();

            User user = userRepository.findById(userId).orElse(null);

            Project project = new Project();
            project.setName(projectName);
            project.setUser(user);
            projectRepository.save(project);
            System.out.println(project);
            return ResponseEntity.ok(project);

        } catch (Exception e){
            System.out.println(e);
            return (ResponseEntity<?>) ResponseEntity.internalServerError();
        }

    }

    @PutMapping("/projectPut")
    public String putProject(@RequestBody String json){
        try {

        }
        catch (Exception e){
            return "Ошибка обработки JSON: " + e.getMessage();
        }
        return json;
    }

    @DeleteMapping("projectDelete/{id}")
    public ResponseEntity<AuthResponse> deleteEntity(@PathVariable Integer id) {

        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            AuthResponse authResponse = new AuthResponse("Success", id.toString());
            return ResponseEntity.ok(authResponse);
        } else {
            AuthResponse authResponse = new AuthResponse("Fail", id.toString());
            return ResponseEntity.ok(authResponse);
        }



    }
}
