package com.example.projectmanagementwebapp.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tab_user")
@Getter
@Setter
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

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Project> userProjects;

}
