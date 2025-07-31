package com.kce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kce.dto.DoctorDto;
import com.kce.service.DoctorService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
@Autowired
	
	private DoctorService doctorService;
	
	
	
@PostMapping(consumes = {"multipart/form-data"})
public ResponseEntity<DoctorDto> createdoctor(
        @RequestPart("doctor") DoctorDto doctorDto,
        @RequestPart(value = "photo", required = false) MultipartFile profilePhoto) {

    DoctorDto savedDoctor = doctorService.createDoctor(doctorDto, profilePhoto);
    return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
}
	
	@GetMapping("/{id}")
	public ResponseEntity<DoctorDto> getDoctorById(@PathVariable("id") String doctorId) {
	    DoctorDto doctorDto = doctorService.getDoctorById(doctorId);
	    return new ResponseEntity<>(doctorDto, HttpStatus.OK);
	}
	@GetMapping
	public ResponseEntity<List<DoctorDto>> getAllDoctors() {
	    List<DoctorDto> doctors = doctorService.getAllDoctors();
	    return new ResponseEntity<>(doctors, HttpStatus.OK);
	}

	
}
