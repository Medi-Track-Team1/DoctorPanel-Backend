package com.kce.repository;

import com.kce.entity.Appointments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointments, String> {

	// Find appointments by doctorId
	List<Appointments> findByDoctorId(String doctorId);

	// Find appointments by doctorId and status
	List<Appointments> findByDoctorIdAndStatus(String doctorId, String status);

	// Find specific appointment by appointmentId and doctorId
	Optional<Appointments> findByAppointmentIdAndDoctorId(String appointmentId, String doctorId);

	// Find appointments by patientId
	List<Appointments> findByPatientId(String patientId);

	// Find appointments by status
	List<Appointments> findByStatus(String status);
}