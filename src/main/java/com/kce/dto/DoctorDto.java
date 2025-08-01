package com.kce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {

	private String id;  // MongoDB _id for update operations (optional but useful)
	private String doctorId;
	private String nmrId;
	private String doctorName;
	private String specialty;
	private String email;
	private String password;
	private String phone;
	private String status;
	private String bio;
	private String education;
	private String experience;
	private String languages; // Comma-separated from frontend
	private String photoUrl;
	private String departmentId;
}
