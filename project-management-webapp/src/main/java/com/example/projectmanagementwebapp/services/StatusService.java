package com.example.projectmanagementwebapp.services;

import com.example.projectmanagementwebapp.entities.Status;
import com.example.projectmanagementwebapp.entities.Task;
import com.example.projectmanagementwebapp.repositories.StatusRepository;
import com.example.projectmanagementwebapp.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StatusService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StatusRepository statusRepository;

//    @Transactional
//    public void updateTaskPositions(Status status, int newPosition) {
//        // Получить все задачи, связанные с данным статусом
//        List<Task> tasks = taskRepository.findAllByStatus(status);
//
//        // Обновить позицию для каждой задачи
//        for (Task task : tasks) {
//            task.setPosition(newPosition);
//        }
//
//        // Сохранить обновленные задачи
//        taskRepository.saveAll(tasks);
//    }
}

