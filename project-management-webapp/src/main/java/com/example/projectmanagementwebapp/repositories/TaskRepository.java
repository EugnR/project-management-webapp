package com.example.projectmanagementwebapp.repositories;

import com.example.projectmanagementwebapp.entities.Project;
import com.example.projectmanagementwebapp.entities.Status;
import com.example.projectmanagementwebapp.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByStatus(Status status);
}
