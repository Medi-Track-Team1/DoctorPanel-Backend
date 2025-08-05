package com.kce.mapper;

import com.kce.dto.PrescriptionDto;
import com.kce.entity.Prescription;

import java.util.stream.Collectors;

public class PrescriptionMapper {

    public static PrescriptionDto mapToPrescriptionDto(Prescription prescription) {
        if (prescription == null) {
            return null;
        }

        PrescriptionDto dto = new PrescriptionDto();
        dto.setPrescriptionId(prescription.getPrescriptionId());
        dto.setAppointmentId(prescription.getAppointmentId());
        dto.setPatientId(prescription.getPatientId());
        dto.setDoctorId(prescription.getDoctorId());
        dto.setPatientName(prescription.getPatientName());
        dto.setDietPlan(prescription.getDietPlan());
        dto.setRecommendedTests(prescription.getRecommendedTests());

        dto.setStatus(prescription.getStatus());

        // Map medicines
        if (prescription.getMedicines() != null) {
            dto.setMedicines(
                prescription.getMedicines().stream()
                    .map(PrescriptionMapper::mapToMedicineDto)
                    .collect(Collectors.toList())
            );
        }

        // Map injections
        if (prescription.getInjections() != null) {
            dto.setInjections(
                prescription.getInjections().stream()
                    .map(PrescriptionMapper::mapToInjectionDto)
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public static Prescription mapToPrescription(PrescriptionDto prescriptionDto) {
        if (prescriptionDto == null) {
            return null;
        }

        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(prescriptionDto.getPrescriptionId());
        prescription.setAppointmentId(prescriptionDto.getAppointmentId());
        prescription.setPatientId(prescriptionDto.getPatientId());
        prescription.setDoctorId(prescriptionDto.getDoctorId());
        prescription.setPatientName(prescriptionDto.getPatientName());
        prescription.setDietPlan(prescriptionDto.getDietPlan());
        prescription.setRecommendedTests(prescriptionDto.getRecommendedTests());

        prescription.setStatus(prescriptionDto.getStatus());

        // Map medicines
        if (prescriptionDto.getMedicines() != null) {
            prescription.setMedicines(
                prescriptionDto.getMedicines().stream()
                    .map(PrescriptionMapper::mapToMedicine)
                    .collect(Collectors.toList())
            );
        }

        // Map injections
        if (prescriptionDto.getInjections() != null) {
            prescription.setInjections(
                prescriptionDto.getInjections().stream()
                    .map(PrescriptionMapper::mapToInjection)
                    .collect(Collectors.toList())
            );
        }

        return prescription;
    }
    
    private static PrescriptionDto.MedicineDto mapToMedicineDto(Prescription.Medicine medicine) {
        if (medicine == null) {
            return null;
        }
        
        PrescriptionDto.MedicineDto dto = new PrescriptionDto.MedicineDto();
        dto.setName(medicine.getName());
        dto.setBatch(medicine.getBatch());
        dto.setDosage(medicine.getDosage());
        dto.setQuantity(medicine.getQuantity());
        dto.setDuration(medicine.getDuration());
        dto.setTiming(medicine.getTiming());
        
        return dto;
    }
    
    private static Prescription.Medicine mapToMedicine(PrescriptionDto.MedicineDto medicineDto) {
        if (medicineDto == null) {
            return null;
        }
        
        Prescription.Medicine medicine = new Prescription.Medicine();
        medicine.setName(medicineDto.getName());
        medicine.setBatch(medicineDto.getBatch());
        medicine.setDosage(medicineDto.getDosage());
        medicine.setQuantity(medicineDto.getQuantity());
        medicine.setDuration(medicineDto.getDuration());
        medicine.setTiming(medicineDto.getTiming());
        
        return medicine;
    }
    
    private static PrescriptionDto.InjectionDto mapToInjectionDto(Prescription.Injection injection) {
        if (injection == null) {
            return null;
        }
        
        PrescriptionDto.InjectionDto dto = new PrescriptionDto.InjectionDto();
        dto.setName(injection.getName());
        dto.setBatch(injection.getBatch());
        dto.setDosage(injection.getDosage());
        dto.setQuantity(injection.getQuantity());
        dto.setSchedule(injection.getSchedule());
        dto.setNotes(injection.getNotes());
        
        return dto;
    }
    
    private static Prescription.Injection mapToInjection(PrescriptionDto.InjectionDto injectionDto) {
        if (injectionDto == null) {
            return null;
        }
        
        Prescription.Injection injection = new Prescription.Injection();
        injection.setName(injectionDto.getName());
        injection.setBatch(injectionDto.getBatch());
        injection.setDosage(injectionDto.getDosage());
        injection.setQuantity(injectionDto.getQuantity());
        injection.setSchedule(injectionDto.getSchedule());
        injection.setNotes(injectionDto.getNotes());
        
        return injection;
    }
}