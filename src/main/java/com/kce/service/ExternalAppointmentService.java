package com.kce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kce.dto.AppointmentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalAppointmentService {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${meditrack.api.base-url:https://your-meditrack-backend-url.onrender.com}")
    private String meditrackBaseUrl;
    
    public ExternalAppointmentService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Get appointment from external MediTrack system
     */
    public AppointmentDto getAppointmentFromMeditrack(String appointmentId) {
        try {
            String url = meditrackBaseUrl + "/api/appointments/" + appointmentId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            System.out.println("🔍 Calling MediTrack API: " + url);
            
            ResponseEntity<MeditrackAppointmentResponse> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                MeditrackAppointmentResponse.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return convertMeditrackToKceAppointment(response.getBody());
            } else {
                throw new RuntimeException("Failed to fetch appointment from MediTrack: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error fetching appointment from MediTrack: " + e.getMessage());
            throw new RuntimeException("Failed to fetch appointment from external system: " + e.getMessage(), e);
        }
    }
    
    /**
     * Mark appointment as completed in external system
     */
    public void markAppointmentCompleted(String appointmentId) {
        try {
            // Get current appointment
            MeditrackAppointmentResponse appointment = getCurrentAppointment(appointmentId);
            
            // Update status to COMPLETED
            appointment.setStatus("COMPLETED");
            
            String url = meditrackBaseUrl + "/api/appointments/" + appointmentId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MeditrackAppointmentResponse> entity = new HttpEntity<>(appointment, headers);
            
            System.out.println("🔄 Marking appointment as completed in MediTrack: " + appointmentId);
            
            ResponseEntity<MeditrackAppointmentResponse> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                MeditrackAppointmentResponse.class
            );
            
            if (response.getStatusCode() != HttpStatus.OK) {
                System.err.println("⚠️ Failed to mark appointment as completed: " + response.getStatusCode());
            } else {
                System.out.println("✅ Successfully marked appointment as completed");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error marking appointment as completed: " + e.getMessage());
            // Don't throw exception here as prescription creation should still succeed
        }
    }
    
    private MeditrackAppointmentResponse getCurrentAppointment(String appointmentId) {
        String url = meditrackBaseUrl + "/api/appointments/" + appointmentId;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<MeditrackAppointmentResponse> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            MeditrackAppointmentResponse.class
        );
        
        return response.getBody();
    }
    
    /**
     * Convert MediTrack appointment format to KCE appointment format
     */
    private AppointmentDto convertMeditrackToKceAppointment(MeditrackAppointmentResponse meditrackApp) {
        AppointmentDto kceApp = new AppointmentDto();
        
        // Map the fields from MediTrack format to KCE format
        kceApp.setAppointmentId(meditrackApp.getAppointmentId()); // Keep original ID
        kceApp.setPatientId(meditrackApp.getPatientId());
        kceApp.setPatientName(meditrackApp.getPatientName());
        kceApp.setDoctorId(meditrackApp.getDoctorId());

        kceApp.setStatus(meditrackApp.getStatus());
        
        System.out.println("🔄 Converted MediTrack appointment to KCE format:");
        System.out.println("   - AppointmentId: " + kceApp.getAppointmentId());
        System.out.println("   - PatientId: " + kceApp.getPatientId());
        System.out.println("   - PatientName: " + kceApp.getPatientName());
        System.out.println("   - DoctorId: " + kceApp.getDoctorId());
        
        return kceApp;
    }
    
    /**
     * MediTrack appointment response format
     */
    public static class MeditrackAppointmentResponse {
        private String appointmentId;
        private String patientId;
        private String patientName;
        private String patientEmail;
        private String doctorId;
        private String doctorName;
        private String appointmentDateTime;
        private String status;
        private Integer duration;
        private String reasonForVisit;
        private String createdAt;
        private String updatedAt;
        
        // Getters and Setters
        public String getAppointmentId() { return appointmentId; }
        public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
        
        public String getPatientId() { return patientId; }
        public void setPatientId(String patientId) { this.patientId = patientId; }
        
        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }
        
        public String getPatientEmail() { return patientEmail; }
        public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }
        
        public String getDoctorId() { return doctorId; }
        public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
        
        public String getDoctorName() { return doctorName; }
        public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
        
        public String getAppointmentDateTime() { return appointmentDateTime; }
        public void setAppointmentDateTime(String appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public Integer getDuration() { return duration; }
        public void setDuration(Integer duration) { this.duration = duration; }
        
        public String getReasonForVisit() { return reasonForVisit; }
        public void setReasonForVisit(String reasonForVisit) { this.reasonForVisit = reasonForVisit; }
        
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        
        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    }
}