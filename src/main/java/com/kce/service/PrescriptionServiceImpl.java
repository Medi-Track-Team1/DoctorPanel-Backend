package com.kce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kce.dto.AppointmentDto;
import com.kce.dto.PrescriptionDto;
import com.kce.entity.Prescription;
import com.kce.mapper.PrescriptionMapper;
import com.kce.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Override
    public PrescriptionDto createPrescription(PrescriptionDto prescriptionDto) {
        try {
            // Store the original appointmentId from frontend (e.g., "apt_1008")
            String originalAppointmentId = prescriptionDto.getAppointmentId();

            // Log the received appointmentId for debugging
            System.out.println("Received appointmentId from frontend: " + originalAppointmentId);
            System.out.println("Received doctorId: " + prescriptionDto.getDoctorId());

            // Get appointment details using your existing method
            AppointmentDto appointment = appointmentService.getAppointmentForDoctor(
                    prescriptionDto.getDoctorId(),
                    originalAppointmentId
            );

            // Log what we got back from the appointment service
            System.out.println("Retrieved appointment: " + appointment);
            System.out.println("Appointment ID from service: " + appointment.getAppointmentId());
            System.out.println("Patient ID: " + appointment.getPatientId());
            System.out.println("Patient Name: " + appointment.getPatientName());

            // Generate prescription ID
            String prescriptionId = generatePrescriptionId();

            // CRITICAL FIX: Always use the original appointmentId from frontend
            // Don't let the appointment service overwrite it
            prescriptionDto.setPrescriptionId(prescriptionId);
            prescriptionDto.setAppointmentId(originalAppointmentId); // Keep original business logic ID
            prescriptionDto.setPatientId(appointment.getPatientId());
            prescriptionDto.setPatientName(appointment.getPatientName());

            prescriptionDto.setStatus("active");

            // Final log before saving
            System.out.println("Final prescription DTO before saving:");
            System.out.println("- PrescriptionId: " + prescriptionDto.getPrescriptionId());
            System.out.println("- AppointmentId: " + prescriptionDto.getAppointmentId());
            System.out.println("- PatientId: " + prescriptionDto.getPatientId());
            System.out.println("- DoctorId: " + prescriptionDto.getDoctorId());

            // Convert to entity and save
            Prescription prescription = PrescriptionMapper.mapToPrescription(prescriptionDto);

            // ADDITIONAL SAFETY CHECK: Ensure the entity has the correct appointmentId
            prescription.setAppointmentId(originalAppointmentId);

            // Log the entity before saving
            System.out.println("Prescription entity appointmentId before save: " + prescription.getAppointmentId());
            System.out.println("🟡 Before saving to MongoDB:");
            System.out.println(new ObjectMapper().writeValueAsString(prescription)); // Jackson
            Prescription savedPrescription = prescriptionRepository.save(prescription);
            System.out.println("✅ After saving to MongoDB:");
            System.out.println(new ObjectMapper().writeValueAsString(savedPrescription));

            // Log after saving
            System.out.println("Saved prescription appointmentId: " + savedPrescription.getAppointmentId());

            // Mark appointment as completed using the original appointmentId
            appointmentService.markCompleted(originalAppointmentId);

            return PrescriptionMapper.mapToPrescriptionDto(savedPrescription);

        } catch (Exception e) {
            System.err.println("Error creating prescription: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create prescription: " + e.getMessage(), e);
        }
    }

    @Override
    public PrescriptionDto getPrescriptionById(String prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + prescriptionId));
        return PrescriptionMapper.mapToPrescriptionDto(prescription);
    }

    @Override
    public PrescriptionDto getPrescriptionByAppointmentId(String appointmentId) {
        System.out.println("📌 Looking up prescription for appointmentId: [" + appointmentId + "]");
        Prescription prescription = prescriptionRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Prescription not found for appointment ID: " + appointmentId));
        return PrescriptionMapper.mapToPrescriptionDto(prescription);
    }


    @Override
    public List<PrescriptionDto> getPrescriptionsByPatientId(String patientId) {
        List<Prescription> prescriptions = prescriptionRepository.findByPatientId(patientId);
        return prescriptions.stream()
                .map(PrescriptionMapper::mapToPrescriptionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDto> getPrescriptionsByDoctorId(String doctorId) {
        List<Prescription> prescriptions = prescriptionRepository.findByDoctorId(doctorId);
        return prescriptions.stream()
                .map(PrescriptionMapper::mapToPrescriptionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDto> getPrescriptionsByPatientAndDoctor(String patientId, String doctorId) {
        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdAndDoctorId(patientId, doctorId);
        return prescriptions.stream()
                .map(PrescriptionMapper::mapToPrescriptionDto)
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionDto updatePrescription(String prescriptionId, PrescriptionDto prescriptionDto) {
        Prescription existingPrescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + prescriptionId));

        // Update fields
        prescriptionDto.setPrescriptionId(prescriptionId);


        Prescription prescription = PrescriptionMapper.mapToPrescription(prescriptionDto);
        Prescription updatedPrescription = prescriptionRepository.save(prescription);

        return PrescriptionMapper.mapToPrescriptionDto(updatedPrescription);
    }

    @Override
    public void deletePrescription(String prescriptionId) {
        if (!prescriptionRepository.existsById(prescriptionId)) {
            throw new RuntimeException("Prescription not found with ID: " + prescriptionId);
        }
        prescriptionRepository.deleteById(prescriptionId);
    }

    @Override
    public List<PrescriptionDto> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return prescriptions.stream()
                .map(PrescriptionMapper::mapToPrescriptionDto)
                .collect(Collectors.toList());
    }

    private String generatePrescriptionId() {
        try {
            long count = prescriptionRepository.count();
            String number = String.format("%04d", count + 1);
            return "PRE-" + number;
        } catch (Exception e) {
            // Fallback to timestamp-based ID
            long timestamp = System.currentTimeMillis() % 10000;
            return "PRE-" + String.format("%04d", timestamp);
        }
    }
}