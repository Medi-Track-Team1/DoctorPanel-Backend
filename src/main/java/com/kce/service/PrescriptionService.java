package com.kce.service;

import com.kce.dto.PrescriptionDto;
import java.util.List;

public interface PrescriptionService {
    
    // Create a new prescription
    PrescriptionDto createPrescription(PrescriptionDto prescriptionDto);
    
    // Get prescription by ID
    PrescriptionDto getPrescriptionById(String prescriptionId);
    
    // Get prescription by appointment ID
    PrescriptionDto getPrescriptionByAppointmentId(String appointmentId);
    
    // Get all prescriptions for a patient
    List<PrescriptionDto> getPrescriptionsByPatientId(String patientId);
    
    // Get all prescriptions by a doctor
    List<PrescriptionDto> getPrescriptionsByDoctorId(String doctorId);
    
    // Get patient's prescriptions by doctor
    List<PrescriptionDto> getPrescriptionsByPatientAndDoctor(String patientId, String doctorId);
    
    // Update prescription
    PrescriptionDto updatePrescription(String prescriptionId, PrescriptionDto prescriptionDto);
    
    // Delete prescription
    void deletePrescription(String prescriptionId);
    
    // Get all prescriptions
    List<PrescriptionDto> getAllPrescriptions();
}