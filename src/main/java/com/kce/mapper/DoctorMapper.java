package com.kce.mapper;

import com.kce.dto.DoctorDto;
import com.kce.entity.Doctor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorMapper {

	public static DoctorDto mapToDoctorDto(Doctor doctor) {
		String languageStr = doctor.getLanguages() != null
				? String.join(", ", doctor.getLanguages())
				: null;

		DoctorDto dto = new DoctorDto();
		dto.setId(doctor.getId()); // 🔁 New line
		dto.setDoctorId(doctor.getDoctorId());
		dto.setNmrId(doctor.getNmrId());
		dto.setDoctorName(doctor.getDoctorName());
		dto.setSpecialty(doctor.getSpecialty());
		dto.setEmail(doctor.getEmail());
		dto.setPassword(doctor.getPassword());
		dto.setPhone(doctor.getPhone());
		dto.setStatus(doctor.getStatus());
		dto.setBio(doctor.getBio());
		dto.setEducation(doctor.getEducation());
		dto.setExperience(doctor.getExperience());
		dto.setLanguages(languageStr);
		dto.setPhotoUrl(doctor.getPhotoUrl());
		dto.setDepartmentId(doctor.getDepartmentId());
		return dto;
	}

	public static Doctor mapToDoctor(DoctorDto doctorDto) {
		List<String> languageList = doctorDto.getLanguages() != null
				? Arrays.stream(doctorDto.getLanguages().split(","))
				.map(String::trim)
				.collect(Collectors.toList())
				: null;

		Doctor doctor = new Doctor();
		doctor.setId(doctorDto.getId()); // 🔁 New line
		doctor.setDoctorId(doctorDto.getDoctorId());
		doctor.setNmrId(doctorDto.getNmrId());
		doctor.setDoctorName(doctorDto.getDoctorName());
		doctor.setSpecialty(doctorDto.getSpecialty());
		doctor.setEmail(doctorDto.getEmail());
		doctor.setPassword(doctorDto.getPassword());
		doctor.setPhone(doctorDto.getPhone());
		doctor.setStatus(doctorDto.getStatus());
		doctor.setBio(doctorDto.getBio());
		doctor.setEducation(doctorDto.getEducation());
		doctor.setExperience(doctorDto.getExperience());
		doctor.setLanguages(languageList);
		doctor.setPhotoUrl(doctorDto.getPhotoUrl());
		doctor.setDepartmentId(doctorDto.getDepartmentId());
		return doctor;
	}
}
