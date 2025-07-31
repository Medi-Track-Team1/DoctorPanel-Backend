package com.kce.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Document(collection = "department")
public class Department {

    @Id
    private String id;

    private String departmentId;
    private String name;

    public Department() {}

    public Department(String departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
    }

    
}
