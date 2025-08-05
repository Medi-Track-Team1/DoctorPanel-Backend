package com.kce.dto;

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
public class PrescriptionDto {
    
    private String prescriptionId;
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String patientName;
    
    private List<MedicineDto> medicines;
    private List<InjectionDto> injections;
    private String dietPlan;
    private List<String> recommendedTests;
    

    private String status;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicineDto {
        private String name;
        private String batch;
        private String dosage;
        private String quantity;
        private String duration;
        private List<String> timing;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InjectionDto {
        private String name;
        private String batch;
        private String dosage;
        private String quantity;
        private String schedule;
        private String notes;
    }
}