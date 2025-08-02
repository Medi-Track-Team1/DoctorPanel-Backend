package com.kce.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="appointment")
public class Appointments {

    @Id
    private String id; // This is the MongoDB _id (auto-generated)

    @Field("appointmentId") // This ensures proper field mapping in MongoDB
    private String appointmentId; // This is your business logic ID (apt_1008, etc.)

    private String patientId;
    private String patientName;
    private int age;
    private String phone;
    private String email;
    private String department;
    private String doctorId;
    private String date;
    private String time;
    private String reason;
    private String notes;
    private String status;

    // Add explicit getter for MongoDB _id if needed
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}