package com.example.projectmanagementwebapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "tab_status")
@Data
public class Status{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_status_id")
    private Integer id;

    @Column(name = "col_status_name")
    private String name;

    @Column(name = "col_status_position")
    private Integer position;

    @JsonIgnore
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @JsonIgnore
    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude // Исключаем из toString
    private List<Task> statusTasks;

}
