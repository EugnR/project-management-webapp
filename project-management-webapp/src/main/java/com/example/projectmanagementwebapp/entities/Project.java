package com.example.projectmanagementwebapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tab_project")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "col_project_id")
    private Long id;

    @Column(name = "col_project_name")
    private String name;

    @ManyToOne      //пользователь владеет проектами
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL) //проект владеет задачами
    private List<Task> projectTasks;


}
