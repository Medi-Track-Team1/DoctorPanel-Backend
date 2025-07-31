package com.kce.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


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

    private String appointmentId;
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
}
