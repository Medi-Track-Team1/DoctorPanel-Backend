package com.kce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopDoctorDto {
    private String id;                    // MongoDB internal ID
    private String doctorId;             // Business ID
    private String nmrId;                // Medical registration ID
    private String doctorName;           // Doctor's full name
    private String specialty;            // Medical specialty
    private String email;                // Email address
    private String password;             // Password (consider excluding in production)
    private String phone;                // Phone number
    private String status;               // Active/Inactive status
    private String bio;                  // Doctor's biography
    private String education;            // Educational background
    private String experience;           // Years of experience
    private String languages;            // Languages spoken (comma-separated)
    private String photoUrl;             // Profile photo URL
    private String departmentId;         // Department ID
    private long prescriptionCount;      // Number of prescriptions created
}