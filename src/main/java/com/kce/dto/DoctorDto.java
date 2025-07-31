package com.kce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
	
	
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
	    private String languages;
	    private String photoUrl;
	    private String departmentId;
}
