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


    @Transactional
    //для переустановки положений после удаления
    public void updateTaskPositions(Integer statusId) {
        // Получить задачу по ID
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Status with id " + statusId + " not found."));

        // Получить все задачи статуса, отсортированные по позиции
        List<Task> tasks = status.getStatusTasks().stream()
                .sorted(Comparator.comparingInt(Task::getPosition))
                .collect(Collectors.toList());

        // Перенумеровать позиции задач
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setPosition(i + 1);
        }

        taskRepository.saveAll(tasks);
    }

    @Transactional
    public void moveTask(Integer taskId, int newStatusId, int newPosition) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with id "
                        + taskId + " not found."));

        Status oldStatus = task.getStatus();

        //если задачу переместили в рамках одного столбца
        if (oldStatus.getId() == newStatusId){
            // Получить все задачи статуса
            List<Task> tasksOfOldStatus = oldStatus.getStatusTasks().stream()
                    .sorted(Comparator.comparingInt(Task::getPosition))
                    .collect(Collectors.toList());
            // Удалить задачу из текущей позиции
            tasksOfOldStatus.remove(task);
            // Добавить задачу в новую позицию
            tasksOfOldStatus.add(newPosition - 1, task);
            // Перенумеровать позиции задач
            for (int i = 0; i < tasksOfOldStatus.size(); i++) {
                tasksOfOldStatus.get(i).setPosition(i + 1);
            }
            // Сохранить обновленные задач
            taskRepository.saveAll(tasksOfOldStatus);
        } else {
            Status newStatus = statusRepository.findById(newStatusId).orElse(null);

            // Получить все задачи прошлого статуса
            List<Task> tasksOfOldStatus = oldStatus.getStatusTasks().stream()
                    .sorted(Comparator.comparingInt(Task::getPosition))
                    .collect(Collectors.toList());
            // Получить все задачи нового статуса
            List<Task> tasksOfNewStatus = newStatus.getStatusTasks().stream()
                    .sorted(Comparator.comparingInt(Task::getPosition))
                    .collect(Collectors.toList());

            // Удалить задачу из текущей позиции
            tasksOfOldStatus.remove(task);
            // Перенумеровать позиции задач в старом статусе
            for (int i = 0; i < tasksOfOldStatus.size(); i++) {
                tasksOfOldStatus.get(i).setPosition(i + 1);
            }

            task.setStatus(newStatus);
            // Добавить задачу в новую позицию нового статуса
            tasksOfNewStatus.add(newPosition - 1, task);
            // Перенумеровать позиции задач
            for (int i = 0; i < tasksOfNewStatus.size(); i++) {
                tasksOfNewStatus.get(i).setPosition(i + 1);
            }

            // Сохранить обновленные задач
            taskRepository.saveAll(tasksOfOldStatus);
            // Сохранить обновленные задач
            taskRepository.saveAll(tasksOfNewStatus);
        }




    }

//    @Transactional
//    public void deleteTask(Integer taskId) {
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new IllegalArgumentException("Task with id " + taskId + " not found."));
//
//        Status status = task.getStatus();
//
//        taskRepository.delete(task);
//    }
}

