package com.kce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kce.dto.AppointmentDto;
import com.kce.service.AppointmentService;

@RestController
@CrossOrigin(
        origins = {
                "http://localhost:5174",
                "http://localhost:5175"
                // Removed any wildcard (*) origins
        },
        allowedHeaders = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RequestMapping("/api/doctor") // ✅ This becomes the prefix for ALL endpoints
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // GET: /api/doctor/appointments
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    // POST: /api/doctor/appointments
    @PostMapping("/appointments")
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        AppointmentDto savedAppointment = appointmentService.createAppointment(appointmentDto);
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    // GET: /api/doctor/{doctorId}/appointments
    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<List<AppointmentDto>> getAllByDoctor(@PathVariable String doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorId));
    }

    // GET: /api/doctor/{doctorId}/appointments/completed
    @GetMapping("/{doctorId}/appointments/completed")
    public ResponseEntity<List<AppointmentDto>> getCompletedAppointments(@PathVariable String doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorIdAndStatus(doctorId, "completed"));
    }

    // PUT: /api/doctor/appointments/{appointmentId}/complete
    @PutMapping("/appointments/{appointmentId}/complete")
    public ResponseEntity<AppointmentDto> markAsCompleted(@PathVariable String appointmentId) {
        AppointmentDto completed = appointmentService.markCompleted(appointmentId);
        return ResponseEntity.ok(completed);
    }

    // OPTIONAL: GET specific appointment for doctor
    // GET: /api/doctor/{doctorId}/appointments/{appointmentId}
    @GetMapping("/{doctorId}/appointments/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentDetails(
            @PathVariable String doctorId,
            @PathVariable String appointmentId) {
        return ResponseEntity.ok(appointmentService.getAppointmentForDoctor(doctorId, appointmentId));
    }
}