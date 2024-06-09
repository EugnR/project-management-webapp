package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.ActionStatusResponse;
import com.example.projectmanagementwebapp.entities.Project;
import com.example.projectmanagementwebapp.entities.Status;
import com.example.projectmanagementwebapp.repositories.ProjectRepository;
import com.example.projectmanagementwebapp.repositories.StatusRepository;
import com.example.projectmanagementwebapp.repositories.TaskRepository;
import com.example.projectmanagementwebapp.services.StatusService;
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
    @Autowired
    private StatusService statusService;


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
            Project project = projectRepository.findById(projectId).orElse(null);
            Integer numOfStatusesInProject = project.getProjectStatuses().size();

            if (project == null){ throw new RuntimeException("Project not found");}

            Status status = new Status();
            status.setName(statusName);
            status.setProject(project);
            status.setPosition(numOfStatusesInProject + 1);
            statusRepository.save(status);
            System.out.println(status);
            return ResponseEntity.ok(status);

        } catch (Exception e){
            System.out.println(e);
            return (ResponseEntity<?>) ResponseEntity.internalServerError();
        }
    }

    @PostMapping("editStatusPosition/{id}/{newPosition}")
    public ResponseEntity<ActionStatusResponse> editStatusPosition(@PathVariable Integer id, @PathVariable Integer newPosition){
        Status status = statusRepository.findById(id).orElse(null);

        if (status != null) {
            statusService.moveStatus(id, newPosition);
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Success", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        } else {
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Fail", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        }
    }
    @PostMapping("editStatusName/{id}")
    public ResponseEntity<ActionStatusResponse> editStatusName(@PathVariable Integer id, @RequestParam("newStatusName") String newName){
        Status status = statusRepository.findById(id).orElse(null);

        if (status != null) {
            status.setName(newName);
            statusRepository.save(status);
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Success", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        } else {
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Fail", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        }
    }


    @DeleteMapping("deleteStatus/{id}")
    public ResponseEntity<ActionStatusResponse> deleteStatus(@PathVariable Integer id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Status with id " + id + " not found."));
        if (statusRepository.existsById(id)) {
            statusService.deleteStatus(id);
            statusService.updateStatusPositions(status.getProject().getId());
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Success", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        } else {
            ActionStatusResponse actionStatusResponse = new ActionStatusResponse("Fail", id.toString());
            return ResponseEntity.ok(actionStatusResponse);
        }
    }



}
