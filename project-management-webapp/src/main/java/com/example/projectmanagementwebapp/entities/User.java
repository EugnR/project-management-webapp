package com.example.projectmanagementwebapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tab_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "col_user_id")
    private Integer id;

    @Column(name = "col_user_name", nullable = false, unique = true)
    private String name;

    @Column(name = "col_user_pass", nullable = false)
    private String password;

    @Column(name = "col_user_email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Project> userProjects;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Task> createdTasks;

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    private List<Task> assignedTasks;


//       вход по одноразовому коду
//    @Column(name = "col_user_password", length = 30, nullable = false)
//    private String password;

}
