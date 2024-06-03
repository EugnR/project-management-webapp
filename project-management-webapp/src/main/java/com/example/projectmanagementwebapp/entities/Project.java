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
//@ToString(exclude = {"user"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "col_project_id")
    private Long id;

    @Column(name = "col_project_name")
    private String name;

    @JsonIgnore
    @ManyToOne      //пользователь владеет проектами
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

//    по проекту находятся статусы проекта
    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL) //проект владеет задачами
    @JsonManagedReference
    private List<Status> projectStatuses;


}
