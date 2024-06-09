package com.example.projectmanagementwebapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "tab_project")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "col_project_id")
    private Integer id;

    @Column(name = "col_project_name")
    private String name;

    @JsonIgnore
    @ManyToOne      //пользователь владеет проектами
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    //проект владеет задачами
    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Status> projectStatuses;


}
