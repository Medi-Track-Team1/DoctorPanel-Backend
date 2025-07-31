package com.kce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.kce.entity.*;
@Repository
public interface AppointmentRepository extends MongoRepository<Appointments,String>{
	Optional<Appointments> findByAppointmentIdAndDoctorId(String appointmentId, String doctorId);

	List<Appointments> findByDoctorId(String doctorId);

	List<Appointments> findByDoctorIdAndStatus(String doctorId, String status);


}
