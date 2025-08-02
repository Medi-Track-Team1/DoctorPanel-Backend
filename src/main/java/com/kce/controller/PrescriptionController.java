package com.kce.controller;

import com.kce.dto.PrescriptionDto;
import com.kce.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/prescriptions")
@CrossOrigin(
        origins = {
                "http://localhost:5174",
                "http://localhost:5175"

        },
        allowedHeaders = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class PrescriptionController {
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    // Create prescription
    @PostMapping
    public ResponseEntity<PrescriptionDto> createPrescription(@RequestBody PrescriptionDto prescriptionDto) {
        try {
            PrescriptionDto createdPrescription = prescriptionService.createPrescription(prescriptionDto);
            return new ResponseEntity<>(createdPrescription, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Get prescription by ID
    @GetMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionDto> getPrescriptionById(@PathVariable String prescriptionId) {
        try {
            PrescriptionDto prescription = prescriptionService.getPrescriptionById(prescriptionId);
            return new ResponseEntity<>(prescription, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    // Get prescription by appointment ID
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PrescriptionDto> getPrescriptionByAppointmentId(@PathVariable String appointmentId) {
        try {
            PrescriptionDto prescription = prescriptionService.getPrescriptionByAppointmentId(appointmentId);
            return new ResponseEntity<>(prescription, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    // Get all prescriptions for a patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByPatientId(@PathVariable String patientId) {
        try {
            List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all prescriptions by a doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByDoctorId(@PathVariable String doctorId) {
        try {
            List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get patient's prescriptions by doctor
    @GetMapping("/patient/{patientId}/doctor/{doctorId}")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByPatientAndDoctor(
            @PathVariable String patientId, 
            @PathVariable String doctorId) {
        try {
            List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsByPatientAndDoctor(patientId, doctorId);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update prescription
    @PutMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionDto> updatePrescription(
            @PathVariable String prescriptionId, 
            @RequestBody PrescriptionDto prescriptionDto) {
        try {
            PrescriptionDto updatedPrescription = prescriptionService.updatePrescription(prescriptionId, prescriptionDto);
            return new ResponseEntity<>(updatedPrescription, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Delete prescription
    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<String> deletePrescription(@PathVariable String prescriptionId) {
        try {
            prescriptionService.deletePrescription(prescriptionId);
            return new ResponseEntity<>("Prescription deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Prescription not found", HttpStatus.NOT_FOUND);
        }
    }
    
    // Get all prescriptions
    @GetMapping
    public ResponseEntity<List<PrescriptionDto>> getAllPrescriptions() {
        try {
            List<PrescriptionDto> prescriptions = prescriptionService.getAllPrescriptions();
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}