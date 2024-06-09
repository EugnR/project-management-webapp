package com.example.projectmanagementwebapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "tab_task")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "col_task_id")
    private Integer id;

    @Column(name = "col_task_name", nullable = false)
    private String name;

    @Column(name = "col_task_description")
    private String description;

    @Column(name = "col_task_position", nullable = false)
    private Integer position;


    @JsonIgnore
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "status_id")
    @ToString.Exclude
    private Status status;

}

