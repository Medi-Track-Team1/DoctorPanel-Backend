package com.kce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kce.entity.Doctor;
@Repository
public interface DoctorRepository extends MongoRepository<Doctor,String>{
	Optional<Doctor> findByDoctorId(String doctorId);
	List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
