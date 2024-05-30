package com.example.projectmanagementwebapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tab_task")
@Data // используйте Lombok для генерации геттеров, сеттеров, equals, hashCode и toString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_task_id")
    private Integer id;

    @Column(name = "col_task_name", nullable = false)
    private String name;

    @Column(name = "col_task_description")
    private String description;

    @Column(name = "col_task_position", nullable = false)
    private Integer position;

//    @Column(name = "col_task_column", nullable = false)
//    private Integer column;


//    @ManyToOne      //в проекте может быть много задач
//    @JoinColumn(name = "project_id")
//    private Project project;

    @JsonIgnore
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private User creator;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "assignee_user_id")
    private User assignee;




//    @Column(name = "col_task_creation-time")
//    private LocalDateTime creationTime;
//
//    @Column(name = "col_task_completion-time")
//    private LocalDateTime completionTime;
//
//    @ManyToOne
//    @JoinColumn(name = "col_task_creator-id")
//    private User creator;
//
//    @ManyToOne
//    @JoinColumn(name = "col_task_assignee-id")
//    private User assignee;
//
//    @ManyToOne
//    @JoinColumn(name = "col_task_tag-id")
//    private Tag tag;
//
//    @ManyToOne
//    @JoinColumn(name = "col_task_priority-id")
//    private Priority priority;
//
//    @ManyToOne
//    @JoinColumn(name = "col_task_status-id")
//    private Status status;
}
