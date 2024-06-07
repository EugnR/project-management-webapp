package com.example.projectmanagementwebapp.services;

import com.example.projectmanagementwebapp.entities.Project;
import com.example.projectmanagementwebapp.entities.Status;
import com.example.projectmanagementwebapp.entities.Task;
import com.example.projectmanagementwebapp.repositories.ProjectRepository;
import com.example.projectmanagementwebapp.repositories.StatusRepository;
import com.example.projectmanagementwebapp.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    ProjectRepository projectRepository;

    //@Transactional
    public void updateTaskPositions(Integer statusId) {
        // Получить status по ID
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Status with id " + statusId + " not found."));

        List<Task> tasks = status.getStatusTasks().stream()
                .sorted(Comparator.comparingInt(Task::getPosition))
                .collect(Collectors.toList());

        // Перенумеровать позиции задач
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setPosition(i + 1);
        }

        // Сохранить обновленные задачи
        taskRepository.saveAll(tasks);
    }

    //    @Transactional
    public void moveTask(Integer taskId, int newPosition) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + taskId + " not found."));

        Status status = task.getStatus();

        // Получить все задачи статуса
        List<Task> tasks = status.getStatusTasks().stream()
                .sorted(Comparator.comparingInt(Task::getPosition))
                .collect(Collectors.toList());

        // Удалить задачу из текущей позиции
        tasks.remove(task);

        // Добавить задачу в новую позицию
        tasks.add(newPosition - 1, task);

        // Перенумеровать позиции задач
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setPosition(i + 1);
        }

        // Сохранить обновленные задач
        taskRepository.saveAll(tasks);
    }

    //@Transactional
    public void deleteTask(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + taskId + " not found."));

        Status status = task.getStatus();

        // Удалить задачу
        taskRepository.delete(task);

//        // Обновить позиции оставшихся статусов
//        updateStatusPositions(project.getId());
    }
}

