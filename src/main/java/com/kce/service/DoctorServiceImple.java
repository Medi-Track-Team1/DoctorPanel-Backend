package com.kce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.kce.dto.DoctorDto;
import com.kce.dto.TopDoctorDto;
import com.kce.entity.Department;
import com.kce.entity.Doctor;
import com.kce.exception.ResourceNotFoundException;
import com.kce.mapper.DoctorMapper;
import com.kce.repository.PrescriptionRepository;
import com.kce.util.DoctorIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.kce.repository.DoctorRepository;
import com.kce.repository.DepartmentRepository;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImple implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private Cloudinary cloudinary;


    @Override
    public DoctorDto createDoctor(DoctorDto doctorDto, MultipartFile profilePhoto) {
        // Convert DTO to entity
        Doctor doctor = DoctorMapper.mapToDoctor(doctorDto);

        // Generate unique doctor ID
        doctor.setDoctorId(DoctorIdGenerator.generateDoctorId());

        // 🔍 Get department using specialty name (not departmentId)
        Department department = departmentRepository
                .findByNameIgnoreCase(doctorDto.getSpecialty())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found for name: " + doctorDto.getSpecialty()));

        doctor.setDepartmentId(department.getDepartmentId());
        doctor.setSpecialty(department.getName());

        // Convert comma-separated languages string to list
        if (doctorDto.getLanguages() != null && !doctorDto.getLanguages().isEmpty()) {
            List<String> languageList = Arrays.stream(doctorDto.getLanguages().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            doctor.setLanguages(languageList);
        }

        doctor.setStatus(doctorDto.getStatus());

        // ☁️ Handle profile photo upload or use photoUrl
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(profilePhoto.getBytes(), ObjectUtils.emptyMap());
                doctor.setPhotoUrl(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Photo upload failed", e);
            }
        } else if (doctorDto.getPhotoUrl() != null && !doctorDto.getPhotoUrl().isEmpty()) {
            doctor.setPhotoUrl(doctorDto.getPhotoUrl());
        }

        // 💾 Save to MongoDB
        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorMapper.mapToDoctorDto(savedDoctor);
    }

    @Override
    public DoctorDto getDoctorById(String doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor is not Exist with given Id: " + doctorId));

        return DoctorMapper.mapToDoctorDto(doctor);
    }

    @Override
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(DoctorMapper::mapToDoctorDto)
                .toList();
    }


    @Override
    public List<DoctorDto> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty)
                .stream()
                .map(DoctorMapper::mapToDoctorDto)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDto updateDoctor(String doctorId, DoctorDto doctorDto) {
        // 🔍 Fetch the existing doctor by doctorId (this ensures we get the MongoDB _id too)
        Doctor existingDoctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        // 🛠 Update only fields, not _id or doctorId
        existingDoctor.setDoctorName(doctorDto.getDoctorName());
        existingDoctor.setEmail(doctorDto.getEmail());
        existingDoctor.setPhone(doctorDto.getPhone());
        existingDoctor.setPassword(doctorDto.getPassword());
        existingDoctor.setBio(doctorDto.getBio());
        existingDoctor.setEducation(doctorDto.getEducation());
        existingDoctor.setExperience(doctorDto.getExperience());
        existingDoctor.setStatus(doctorDto.getStatus());
        existingDoctor.setNmrId(doctorDto.getNmrId());

        if (doctorDto.getLanguages() != null) {
            List<String> languageList = Arrays.stream(doctorDto.getLanguages().split(","))
                    .map(String::trim)
                    .toList();
            existingDoctor.setLanguages(languageList);
        }

        if (doctorDto.getSpecialty() != null) {
            Department department = departmentRepository.findByNameIgnoreCase(doctorDto.getSpecialty())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found for specialty: " + doctorDto.getSpecialty()));
            existingDoctor.setDepartmentId(department.getDepartmentId());
            existingDoctor.setSpecialty(department.getName());
        }

        if (doctorDto.getPhotoUrl() != null) {
            existingDoctor.setPhotoUrl(doctorDto.getPhotoUrl());
        }

        // ✅ This save will perform an UPDATE because _id is preserved
        Doctor updated = doctorRepository.save(existingDoctor);
        return DoctorMapper.mapToDoctorDto(updated);
    }


    @Override
    public void deleteDoctor(String doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        doctorRepository.delete(doctor);
    }

    @Override
    public long getDoctorCount() {
        return doctorRepository.count();
    }

    @Override
    public List<TopDoctorDto> getTop5DoctorsByPrescriptions() {
        try {
            System.out.println("🔍 Starting getTop5DoctorsByPrescriptions...");

            // Get all doctors
            List<Doctor> allDoctors = doctorRepository.findAll();
            System.out.println("📊 Found " + allDoctors.size() + " doctors in database");

            if (allDoctors.isEmpty()) {
                System.out.println("⚠️ No doctors found in database");
                return new ArrayList<>();
            }

            // Create a list with prescription counts
            List<TopDoctorDto> doctorsWithCounts = new ArrayList<>();

            for (Doctor doctor : allDoctors) {
                try {
                    // Count prescriptions for each doctor
                    long prescriptionCount = prescriptionRepository.countByDoctorId(doctor.getDoctorId());
                    System.out.println("👨‍⚕️ Doctor: " + doctor.getDoctorName() + " (" + doctor.getDoctorId() + ") - Prescriptions: " + prescriptionCount);

                    if (prescriptionCount > 0) { // Only include doctors with prescriptions
                        TopDoctorDto topDoctorDto = new TopDoctorDto();
                        topDoctorDto.setId(doctor.getId());
                        topDoctorDto.setDoctorId(doctor.getDoctorId());
                        topDoctorDto.setNmrId(doctor.getNmrId());
                        topDoctorDto.setDoctorName(doctor.getDoctorName());
                        topDoctorDto.setSpecialty(doctor.getSpecialty());
                        topDoctorDto.setEmail(doctor.getEmail());
                        topDoctorDto.setPassword(doctor.getPassword()); // Consider removing in production
                        topDoctorDto.setPhone(doctor.getPhone());
                        topDoctorDto.setStatus(doctor.getStatus());
                        topDoctorDto.setBio(doctor.getBio());
                        topDoctorDto.setEducation(doctor.getEducation());
                        topDoctorDto.setExperience(doctor.getExperience());

                        // Convert languages list to comma-separated string
                        String languageStr = doctor.getLanguages() != null
                                ? String.join(", ", doctor.getLanguages())
                                : null;
                        topDoctorDto.setLanguages(languageStr);

                        topDoctorDto.setPhotoUrl(doctor.getPhotoUrl());
                        topDoctorDto.setDepartmentId(doctor.getDepartmentId());
                        topDoctorDto.setPrescriptionCount(prescriptionCount);

                        doctorsWithCounts.add(topDoctorDto);
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error processing doctor " + doctor.getDoctorId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("📈 Found " + doctorsWithCounts.size() + " doctors with prescriptions");

            // Sort by prescription count (descending) and limit to top 5
            List<TopDoctorDto> top5Doctors = doctorsWithCounts.stream()
                    .sorted((d1, d2) -> Long.compare(d2.getPrescriptionCount(), d1.getPrescriptionCount()))
                    .limit(5)
                    .collect(Collectors.toList());

            System.out.println("🏆 Returning top " + top5Doctors.size() + " doctors");
            for (TopDoctorDto doctor : top5Doctors) {
                System.out.println("   - " + doctor.getDoctorName() + ": " + doctor.getPrescriptionCount() + " prescriptions");
            }

            return top5Doctors;

        } catch (Exception e) {
            System.err.println("❌ Error getting top doctors by prescriptions: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get top doctors by prescriptions: " + e.getMessage(), e);
        }
    }
}




