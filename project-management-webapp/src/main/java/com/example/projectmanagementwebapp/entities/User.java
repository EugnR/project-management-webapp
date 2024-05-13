package com.example.projectmanagementwebapp.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tab_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_user_id")
    private Integer id;

    @Column(name = "col_user_name", length = 30, nullable = false)
    private String name;

    @Column(name = "col_user_email", length = 30, nullable = false)
    private String email;

    @Column(name = "col_user_password", length = 30, nullable = false)
    private String password;

}
