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


    @Autowired
    private ExternalAppointmentService externalAppointmentService; // ADD THIS

    @Override
    public PrescriptionDto createPrescription(PrescriptionDto prescriptionDto) {
        try {
            // Remove all appointment lookup and validation logic
            // String originalAppointmentId = prescriptionDto.getAppointmentId();
            // String doctorId = prescriptionDto.getDoctorId();
            // AppointmentDto appointment = null;
            // ...externalAppointmentService.getAppointmentFromMeditrack...
            // ...appointmentService.getAppointmentForDoctor...
            // ...doctorId mismatch check...
            // ...patientId/name/status set from appointment...

            // Generate prescription ID
            String prescriptionId = generatePrescriptionId();
            prescriptionDto.setPrescriptionId(prescriptionId);

            // Optionally set status
            prescriptionDto.setStatus("active");

            // Convert to entity and save
            Prescription prescription = PrescriptionMapper.mapToPrescription(prescriptionDto);
            prescription.setAppointmentId(prescriptionDto.getAppointmentId());

            Prescription savedPrescription = prescriptionRepository.save(prescription);

            // Optionally, you can skip marking appointment as completed

            return PrescriptionMapper.mapToPrescriptionDto(savedPrescription);

        } catch (Exception e) {
            System.err.println("❌ Error creating prescription: " + e.getMessage());
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