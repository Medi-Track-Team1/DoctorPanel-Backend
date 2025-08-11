package com.kce.controller;

import java.util.List;

import com.kce.dto.TopDoctorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.kce.dto.DoctorDto;
import com.kce.service.DoctorService;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController

@RequestMapping("/api/doctor")
@CrossOrigin(
		origins = {
				"http://localhost:5174",
				"http://localhost:5175",
		      	"*"

		},
		allowedHeaders = "*",
		allowCredentials = "true",
		methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
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
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DoctorDto> createDoctorJson(@RequestBody DoctorDto doctorDto) {
		return new ResponseEntity<>(doctorService.createDoctor(doctorDto, null), HttpStatus.CREATED);
	}

	@GetMapping("/by-specialty")
	public ResponseEntity<List<DoctorDto>> getDoctorsBySpecialty(@RequestParam String specialty) {
		return ResponseEntity.ok(doctorService.getDoctorsBySpecialty(specialty));
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
	@PutMapping("/{id}")
	public ResponseEntity<DoctorDto> updateDoctor(
			@PathVariable("id") String doctorId,
			@RequestBody DoctorDto doctorDto) {
		DoctorDto updatedDoctor = doctorService.updateDoctor(doctorId, doctorDto);
		return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDoctor(@PathVariable("id") String doctorId) {
		doctorService.deleteDoctor(doctorId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	@GetMapping("/count")
	public ResponseEntity<Long> getDoctorCount() {
		long count = doctorService.getDoctorCount();
		return ResponseEntity.ok(count);
	}
	@GetMapping("/top-doctors-by-prescriptions")
	public ResponseEntity<?> getTop5DoctorsByPrescriptions() {
		try {
			List<TopDoctorDto> topDoctors = doctorService.getTop5DoctorsByPrescriptions();

			if (topDoctors.isEmpty()) {
				return ResponseEntity.ok()
						.body(createSuccessResponse("No doctors found with prescriptions", topDoctors));
			}

			return ResponseEntity.ok(topDoctors);

		} catch (Exception e) {
			System.err.println("Error fetching top doctors by prescriptions: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(createErrorResponse("Failed to fetch top doctors: " + e.getMessage()));
		}
	}

	// Helper methods for consistent responses
	private java.util.Map<String, Object> createErrorResponse(String message) {
		java.util.Map<String, Object> response = new java.util.HashMap<>();
		response.put("success", false);
		response.put("message", message);
		response.put("timestamp", System.currentTimeMillis());
		return response;
	}

	private java.util.Map<String, Object> createSuccessResponse(String message, Object data) {
		java.util.Map<String, Object> response = new java.util.HashMap<>();
		response.put("success", true);
		response.put("message", message);
		response.put("data", data);
		response.put("timestamp", System.currentTimeMillis());
		return response;
	}

}


