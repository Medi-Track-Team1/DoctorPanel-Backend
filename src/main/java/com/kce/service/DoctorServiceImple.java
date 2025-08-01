package com.kce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.kce.dto.DoctorDto;
import com.kce.entity.Department;
import com.kce.entity.Doctor;
import com.kce.exception.ResourceNotFoundException;
import com.kce.mapper.DoctorMapper;
import com.kce.util.DoctorIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.kce.repository.DoctorRepository;
import com.kce.repository.DepartmentRepository;

import java.io.IOException;
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


}
