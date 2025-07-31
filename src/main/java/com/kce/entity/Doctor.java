package com.kce.entity;

import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="doctors")
public class Doctor {

    
    private String doctorId;
    private String nmrId;
    private String doctorName;
    private String specialty;
    private String email;
    private String password;
    private String phone;
    private String status; 
    private String bio;
    private String education;
    private String experience;
    private String languages;
    private String photoUrl;
    private String departmentId;
    
}
