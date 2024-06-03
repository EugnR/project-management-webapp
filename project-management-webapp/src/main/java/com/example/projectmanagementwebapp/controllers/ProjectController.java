package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.AuthResponse;
import com.example.projectmanagementwebapp.entities.Project;
import com.example.projectmanagementwebapp.entities.Status;
import com.example.projectmanagementwebapp.entities.User;
import com.example.projectmanagementwebapp.repositories.ProjectRepository;
import com.example.projectmanagementwebapp.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/getAllProjects")
    public List<Project> getAllProjects(){
        List<Project> projectList = projectRepository.findAll();
        return projectList;
    }

//    @PostMapping("/projectGet")
//    public List<Project> processGet(@RequestBody String json){
//        try {
//            ObjectMapper requestMapper = new ObjectMapper();
//            JsonNode rootNode = requestMapper.readTree(json);
//
//            Integer userId= rootNode.get("user").asInt();
//
//            User user = userRepository.findById(userId).orElse(null);
//
//            return projectRepository.findAllByUser(user);
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//            return new ArrayList<>();
//        }
//
//        //return "0";
//    }

    @PostMapping("/getProjectsByUserId/{userId}")
    public ResponseEntity<?> processGet(@PathVariable Integer userId){

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            List<Project> projectList = projectRepository.findAllByUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(projectList);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projects not found");
        }

    }

    @PostMapping("/createProject")
    public ResponseEntity<?> postProject(@RequestBody String json){
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            JsonNode rootNode = requestMapper.readTree(json);

            String projectName = rootNode.get("name").asText();
            Integer userId= rootNode.get("userId").asInt();

            User user = userRepository.findById(userId).orElse(null);
            //ПОСЛЕ ДЕБАГА ВЕРНУТЬ!!!!
            //            if (user == null){ throw new RuntimeException("User not found");}

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

    @DeleteMapping("deleteProject/{id}")
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
