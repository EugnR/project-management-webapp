package com.example.projectmanagementwebapp.controllers;

import com.example.projectmanagementwebapp.entities.User;
import com.example.projectmanagementwebapp.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/example/")
public class TestController {

    @Autowired
    UserRepository userRepository;
    //region что приходит с фронта
    //Запрос на создание задачи
    //имя - Untitled
    //описание - Description
    //создатель - id создателя проекта
    //исполнитель - id создателя проекта
    //номер проекта - temp 22
    //endregion
    @GetMapping("/testget")
    public String processGet(){
        try {
            ObjectMapper jsonAssemblerMapper = new ObjectMapper();       //для сериализации в самом конце

            String project1 = "project1";
            String project2 = "project2";
            String project3 = "project3";
            String project4 = "project4";
            String project5 = "project5";


            Map<String, String> outputJson = new HashMap<>();
            outputJson.put("pr1", project1);
            outputJson.put("pr2", project2);
            outputJson.put("pr3", project3);
            outputJson.put("pr4", project4);
            outputJson.put("pr5", project5);

            return jsonAssemblerMapper.writeValueAsString(outputJson);
        } catch (Exception e){
            return e.getMessage();
        }

        //return "0";
    }

//    @PostMapping("/testpost")
//    public String processPost(@RequestBody User user){
//        System.out.print(user);
//
//        return "ОТвет от бэка - успешно!";
//    }

    @PostMapping("/testpost")
    public ResponseEntity<User> processPost(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        System.out.println(user);


    return ResponseEntity.ok(user);
    }

    @PutMapping("/testput")
    public String processPut(@RequestBody String json){
        return "0";
    }

    @DeleteMapping
    public String processDelete(){
        return "0";
    }



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





}
