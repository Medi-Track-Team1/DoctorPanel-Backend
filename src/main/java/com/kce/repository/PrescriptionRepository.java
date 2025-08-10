package com.kce.repository;

import com.kce.entity.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    // Existing methods...
    Optional<Prescription> findByAppointmentId(String appointmentId);
    List<Prescription> findByPatientId(String patientId);
    List<Prescription> findByDoctorId(String doctorId);
    List<Prescription> findByPatientIdAndDoctorId(String patientId, String doctorId);
    List<Prescription> findByStatus(String status);
    long count();

    // New method: Count prescriptions by doctorId
    long countByDoctorId(String doctorId);

    // Simple method to get all distinct doctorIds
    @Query(value = "{}", fields = "{ doctorId: 1, _id: 0 }")
    List<Prescription> findAllDoctorIds();
}
