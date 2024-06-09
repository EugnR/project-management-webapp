package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.ActionStatusResponse;
import com.example.projectmanagementwebapp.entities.Project;
import com.example.projectmanagementwebapp.entities.User;
import com.example.projectmanagementwebapp.repositories.ProjectRepository;
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


    @GetMapping("/getProjectsByUserId/{userId}")
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

            if (user == null){ throw new RuntimeException("User not found");}

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


    @DeleteMapping("deleteProject/{id}")
    public ResponseEntity<ActionStatusResponse> deleteEntity(@PathVariable Integer id) {

        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Success", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        } else {
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Fail", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        }
    }
}
