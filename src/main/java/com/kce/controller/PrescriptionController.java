package com.kce.controller;

import com.kce.dto.PrescriptionDto;
import com.kce.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(
        origins = {
                "http://localhost:5174",
                "http://localhost:5175"
                // Removed "https://your-frontend-domain.com" and any wildcard origins
        },
        allowedHeaders = "*",
        allowCredentials = "true",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        }
)
public class PrescriptionController {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionController.class);

    @Autowired
    private PrescriptionService prescriptionService;

    // Create prescription with detailed error handling
    @PostMapping
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionDto prescriptionDto) {
        try {
            logger.info("Creating prescription for appointmentId: {}, doctorId: {}",
                    prescriptionDto.getAppointmentId(), prescriptionDto.getDoctorId());

            // Validate required fields
            if (prescriptionDto.getAppointmentId() == null || prescriptionDto.getAppointmentId().trim().isEmpty()) {
                logger.error("Appointment ID is required");
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Appointment ID is required"));
            }

            if (prescriptionDto.getDoctorId() == null || prescriptionDto.getDoctorId().trim().isEmpty()) {
                logger.error("Doctor ID is required");
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Doctor ID is required"));
            }

            // Validate that at least one prescription item exists
            if ((prescriptionDto.getMedicines() == null || prescriptionDto.getMedicines().isEmpty()) &&
                    (prescriptionDto.getInjections() == null || prescriptionDto.getInjections().isEmpty()) &&
                    (prescriptionDto.getDietPlan() == null || prescriptionDto.getDietPlan().trim().isEmpty()) &&
                    (prescriptionDto.getRecommendedTests() == null || prescriptionDto.getRecommendedTests().isEmpty())) {
                logger.error("At least one prescription item is required");
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("At least one medicine, injection, diet plan, or test recommendation is required"));
            }

            PrescriptionDto createdPrescription = prescriptionService.createPrescription(prescriptionDto);
            logger.info("Prescription created successfully with ID: {}", createdPrescription.getPrescriptionId());

            return new ResponseEntity<>(createdPrescription, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            logger.error("Validation error creating prescription: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("Validation error: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Error creating prescription: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to create prescription: " + e.getMessage()));
        }
    }

    // Get prescription by ID
    @GetMapping("/{prescriptionId}")
    public ResponseEntity<?> getPrescriptionById(@PathVariable String prescriptionId) {
        try {
            logger.info("Fetching prescription by ID: {}", prescriptionId);
            PrescriptionDto prescription = prescriptionService.getPrescriptionById(prescriptionId);
            return new ResponseEntity<>(prescription, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching prescription by ID {}: {}", prescriptionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Prescription not found: " + e.getMessage()));
        }
    }

    // Get prescription by appointment ID
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionByAppointmentId(@PathVariable String appointmentId) {
        try {
            logger.info("Fetching prescription by appointment ID: {}", appointmentId);
            PrescriptionDto prescription = prescriptionService.getPrescriptionByAppointmentId(appointmentId);
            return new ResponseEntity<>(prescription, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching prescription by appointment ID {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Prescription not found for appointment: " + e.getMessage()));
        }
    }

    // Get all prescriptions for a patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPrescriptionsByPatientId(@PathVariable String patientId) {
        try {
            logger.info("Fetching prescriptions for patient ID: {}", patientId);
            List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching prescriptions for patient {}: {}", patientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching patient prescriptions: " + e.getMessage()));
        }
    }

    // Get all prescriptions by a doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getPrescriptionsByDoctorId(@PathVariable String doctorId) {
        try {
            logger.info("Fetching prescriptions for doctor ID: {}", doctorId);
            List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching prescriptions for doctor {}: {}", doctorId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching doctor prescriptions: " + e.getMessage()));
        }
    }

    // Get patient's prescriptions by doctor
    @GetMapping("/patient/{patientId}/doctor/{doctorId}")
    public ResponseEntity<?> getPrescriptionsByPatientAndDoctor(
            @PathVariable String patientId,
            @PathVariable String doctorId) {
        try {
            logger.info("Fetching prescriptions for patient {} and doctor {}", patientId, doctorId);
            List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsByPatientAndDoctor(patientId, doctorId);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching prescriptions for patient {} and doctor {}: {}", patientId, doctorId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching prescriptions: " + e.getMessage()));
        }
    }

    // Update prescription
    @PutMapping("/{prescriptionId}")
    public ResponseEntity<?> updatePrescription(
            @PathVariable String prescriptionId,
            @RequestBody PrescriptionDto prescriptionDto) {
        try {
            logger.info("Updating prescription ID: {}", prescriptionId);
            PrescriptionDto updatedPrescription = prescriptionService.updatePrescription(prescriptionId, prescriptionDto);
            return new ResponseEntity<>(updatedPrescription, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating prescription {}: {}", prescriptionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Error updating prescription: " + e.getMessage()));
        }
    }

    // Delete prescription
    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<?> deletePrescription(@PathVariable String prescriptionId) {
        try {
            logger.info("Deleting prescription ID: {}", prescriptionId);
            prescriptionService.deletePrescription(prescriptionId);
            return ResponseEntity.ok(createSuccessResponse("Prescription deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting prescription {}: {}", prescriptionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Error deleting prescription: " + e.getMessage()));
        }
    }

    // Get all prescriptions
    @GetMapping
    public ResponseEntity<?> getAllPrescriptions() {
        try {
            logger.info("Fetching all prescriptions");
            List<PrescriptionDto> prescriptions = prescriptionService.getAllPrescriptions();
            logger.info("Found {} prescriptions", prescriptions.size());
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching all prescriptions: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching prescriptions: " + e.getMessage()));
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(createSuccessResponse("Prescription service is running"));
    }

    // Helper methods for consistent error responses
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    private Map<String, Object> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}