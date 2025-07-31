package com.kce.mapper;

import com.kce.dto.DoctorDto;
import com.kce.entity.Doctor;

public class DoctorMapper {

	public static DoctorDto mapToDoctorDto(Doctor doctor) {
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
				doctor.getLanguages(),
				doctor.getPhotoUrl(),
				doctor.getDepartmentId()
				
				);
	}
	public static Doctor mapToDoctor(DoctorDto doctorDto) {
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
				doctorDto.getLanguages(),
				doctorDto.getPhotoUrl(),
				doctorDto.getDepartmentId()
				);
	}
}
