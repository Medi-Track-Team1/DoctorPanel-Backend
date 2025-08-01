package com.kce.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "prescriptions")
public class Prescription {
    
    @Id
    private String prescriptionId; // Format: PRE-XXXX
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String patientName;
    
    private List<Medicine> medicines;
    private List<Injection> injections;
    private String dietPlan;
    private List<String> recommendedTests;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status; // active, inactive
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Medicine {
        private String name;
        private String batch;
        private String dosage;
        private String quantity;
        private String duration;
        private List<String> timing; // Morning, Afternoon, Night
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Injection {
        private String name;
        private String batch;
        private String dosage;
        private String quantity;
        private String schedule;
        private String notes;
    }
}