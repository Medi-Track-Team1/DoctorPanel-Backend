package com.kce.repository;

import com.kce.entity.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    
    // Find prescriptions by appointmentId
    Optional<Prescription> findByAppointmentId(String appointmentId);
    
    // Find prescriptions by patientId
    List<Prescription> findByPatientId(String patientId);
    
    // Find prescriptions by doctorId
    List<Prescription> findByDoctorId(String doctorId);
    
    // Find prescriptions by patientId and doctorId
    List<Prescription> findByPatientIdAndDoctorId(String patientId, String doctorId);
    
    // Find prescriptions by status
    List<Prescription> findByStatus(String status);
    
    // Count prescriptions for ID generation
    long count();
}