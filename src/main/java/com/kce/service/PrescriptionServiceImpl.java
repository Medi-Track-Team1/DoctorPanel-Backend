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
            String originalAppointmentId = prescriptionDto.getAppointmentId();
            String doctorId = prescriptionDto.getDoctorId();

            System.out.println("🔍 Creating prescription for appointmentId: " + originalAppointmentId + ", doctorId: " + doctorId);

            AppointmentDto appointment = null;

            // Try to get appointment from external MediTrack system first
            try {
                appointment = externalAppointmentService.getAppointmentFromMeditrack(originalAppointmentId);
                System.out.println("✅ Found appointment in external MediTrack system");

                // Verify the doctor matches
                if (!doctorId.equals(appointment.getDoctorId())) {
                    throw new RuntimeException("Doctor ID mismatch. Expected: " + doctorId + ", Found: " + appointment.getDoctorId());
                }

            } catch (Exception externalException) {
                System.err.println("⚠️ Appointment not found in external MediTrack system: " + externalException.getMessage());

                // Fallback: Try local KCE system
                try {
                    appointment = appointmentService.getAppointmentForDoctor(doctorId, originalAppointmentId);
                    System.out.println("✅ Found appointment in local KCE system");
                } catch (Exception localException) {
                    System.err.println("❌ Appointment not found in local system either: " + localException.getMessage());
                    throw new RuntimeException("Appointment not found with ID: " + originalAppointmentId + " for doctor: " + doctorId +
                            ". Checked both external MediTrack and local KCE systems.");
                }
            }

            // Generate prescription ID
            String prescriptionId = generatePrescriptionId();

            // Set prescription details
            prescriptionDto.setPrescriptionId(prescriptionId);
            prescriptionDto.setAppointmentId(originalAppointmentId);
            prescriptionDto.setPatientId(appointment.getPatientId());
            prescriptionDto.setPatientName(appointment.getPatientName());
            prescriptionDto.setStatus("active");

            System.out.println("📝 Final prescription details:");
            System.out.println("   - PrescriptionId: " + prescriptionDto.getPrescriptionId());
            System.out.println("   - AppointmentId: " + prescriptionDto.getAppointmentId());
            System.out.println("   - PatientId: " + prescriptionDto.getPatientId());
            System.out.println("   - PatientName: " + prescriptionDto.getPatientName());

            // Convert to entity and save
            Prescription prescription = PrescriptionMapper.mapToPrescription(prescriptionDto);
            prescription.setAppointmentId(originalAppointmentId);

            System.out.println("💾 Saving prescription to database...");
            Prescription savedPrescription = prescriptionRepository.save(prescription);

            System.out.println("✅ Prescription saved successfully with ID: " + savedPrescription.getPrescriptionId());

            // Mark appointment as completed in external system
            try {
                externalAppointmentService.markAppointmentCompleted(originalAppointmentId);
                System.out.println("✅ Marked appointment as completed in external MediTrack system");
            } catch (Exception markException) {
                System.err.println("⚠️ Could not mark appointment as completed: " + markException.getMessage());
                // Don't throw exception here as prescription creation was successful
            }

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