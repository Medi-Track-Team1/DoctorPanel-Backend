package com.kce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private String appointmentId;
    private String patientId;
    private String patientName;
    private int age;
    private String phone;
    private String email;
   
    private String department;
    private String doctorId;
    private String date; // Format: yyyy-MM-dd
    private String time; // Format: HH:mm

    private String reason;
    private String notes;
    private String status;
}
