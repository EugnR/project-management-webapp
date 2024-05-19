package com.example.projectmanagementwebapp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {


    @GetMapping("/projectGet")
    public String getProject(){
        return "0";
    }

    @PostMapping("/projectPost")
    public String postProject(){
        return "0";
    }

    //region что приходит с фронта
    //Запрос на создание проекта
    //имя - Untitled
    //создатель - id создателя проекта
    //endregion
    @PutMapping("/projectPut")
    public String putProject(@RequestBody String json){
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            JsonNode rootNode = requestMapper.readTree(json);
            ObjectMapper jsonAssemblerMapper = new ObjectMapper();       //для сериализации в самом конце

            String taskName = rootNode.get("taskName").asText();
            String taskDescription = rootNode.get("taskDescription").asText();
            String taskCreator = rootNode.get("taskCreator").asText();
            String taskAssignee = rootNode.get("taskAssignee").asText();
            String taskProject = rootNode.get("taskProject").asText();


            Map<String, String> outputJson = new HashMap<>();
            outputJson.put("gottenName", taskName);
            outputJson.put("gottenDescription", taskDescription);
            outputJson.put("gottenCreator", taskCreator);
            outputJson.put("gottenAssignee", taskAssignee);
            outputJson.put("gottenProject", taskProject);

            return jsonAssemblerMapper.writeValueAsString(outputJson);
        }
        catch (Exception e){
            return "Ошибка обработки JSON: " + e.getMessage();
        }
    }

    @DeleteMapping
    public String deleteProject(){
        return "0";
    }
}
