package com.example.projectmanagementwebapp.repositories;

import com.example.projectmanagementwebapp.entities.Project;
import com.example.projectmanagementwebapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findAllByUser(User user);


}
