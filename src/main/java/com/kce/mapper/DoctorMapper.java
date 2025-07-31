package com.kce.mapper;

import com.kce.dto.DoctorDto;
import com.kce.entity.Doctor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorMapper {

	public static DoctorDto mapToDoctorDto(Doctor doctor) {
		// Convert List<String> to comma-separated String
		String languageStr = doctor.getLanguages() != null
				? String.join(", ", doctor.getLanguages())
				: null;

		return new DoctorDto(
				doctor.getDoctorId(),
				doctor.getNmrId(),
				doctor.getDoctorName(),
				doctor.getSpecialty(),
				doctor.getEmail(),
				doctor.getPassword(),
				doctor.getPhone(),
				doctor.getStatus(),
				doctor.getBio(),
				doctor.getEducation(),
				doctor.getExperience(),
				languageStr, // 🔁 Set as String
				doctor.getPhotoUrl(),
				doctor.getDepartmentId()
		);
	}

	public static Doctor mapToDoctor(DoctorDto doctorDto) {
		// Convert comma-separated String to List<String>
		List<String> languageList = doctorDto.getLanguages() != null
				? Arrays.stream(doctorDto.getLanguages().split(","))
				.map(String::trim)
				.collect(Collectors.toList())
				: null;

		return new Doctor(
				doctorDto.getDoctorId(),
				doctorDto.getNmrId(),
				doctorDto.getDoctorName(),
				doctorDto.getSpecialty(),
				doctorDto.getEmail(),
				doctorDto.getPassword(),
				doctorDto.getPhone(),
				doctorDto.getStatus(),
				doctorDto.getBio(),
				doctorDto.getEducation(),
				doctorDto.getExperience(),
				languageList, // 🔁 Set as List<String>
				doctorDto.getPhotoUrl(),
				doctorDto.getDepartmentId()
		);
	}
}
