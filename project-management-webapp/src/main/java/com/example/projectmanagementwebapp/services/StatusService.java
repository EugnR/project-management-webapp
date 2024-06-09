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
public class StatusService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Transactional
    //для переустановки положений после удаления
    public void updateStatusPositions(Integer projectId) {
    // Получить проект по ID
    Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Project with id " + projectId + " not found."));

    // Получить все статусы проекта, отсортированные по позиции
    List<Status> statuses = project.getProjectStatuses().stream()
            .sorted(Comparator.comparingInt(Status::getPosition))
            .collect(Collectors.toList());

    // Перенумеровать позиции статусов
    for (int i = 0; i < statuses.size(); i++) {
        statuses.get(i).setPosition(i + 1);
    }
    // Сохранить обновленные статусы
    statusRepository.saveAll(statuses);
}

    @Transactional
    public void moveStatus(Integer statusId, int newPosition) {
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Status with id " + statusId + " not found."));

        Project project = status.getProject();

        // Получить все статусы проекта
        List<Status> statuses = project.getProjectStatuses().stream()
                .sorted(Comparator.comparingInt(Status::getPosition))
                .collect(Collectors.toList());

        // Удалить статус из текущей позиции
        statuses.remove(status);

        // Добавить статус в новую позицию
        statuses.add(newPosition - 1, status);

        // Перенумеровать позиции статусов
        for (int i = 0; i < statuses.size(); i++) {
            statuses.get(i).setPosition(i + 1);
        }

        // Сохранить обновленные статусы
        statusRepository.saveAll(statuses);
    }

    @Transactional
    public void deleteStatus(Integer statusId) {
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Status with id " + statusId + " not found."));

        Project project = status.getProject();

        statusRepository.delete(status);
    }
}

