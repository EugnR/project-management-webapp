package com.example.projectmanagementwebapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionStatusResponse {
    private String status;
    private String id;
}
