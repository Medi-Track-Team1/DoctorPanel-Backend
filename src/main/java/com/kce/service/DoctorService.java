package com.kce.service;

import java.util.List;

import com.kce.dto.TopDoctorDto;
import org.springframework.web.multipart.MultipartFile;
import com.kce.dto.DoctorDto;

public interface DoctorService {
	DoctorDto createDoctor(DoctorDto doctorDto, MultipartFile profilePhoto);

	DoctorDto getDoctorById(String doctorId);

	List<DoctorDto> getAllDoctors();

	List<DoctorDto> getDoctorsBySpecialty(String specialty);

	DoctorDto updateDoctor(String doctorId, DoctorDto doctorDto); // 🆕 Added

	void deleteDoctor(String doctorId); // 🆕 Added
	long getDoctorCount();
	List<TopDoctorDto> getTop5DoctorsByPrescriptions();
}
