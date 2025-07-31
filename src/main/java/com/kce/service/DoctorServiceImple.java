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
import com.kce.config.CloudinaryConfig.*;

import org.springframework.web.multipart.MultipartFile;
import com.kce.repository.DoctorRepository;
import com.kce.repository.DepartmentRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

        // 🔍 Get department by name (specialty)
        Department department = departmentRepository
        	    .findByDepartmentId(doctorDto.getDepartmentId())
        	    .orElseThrow(() -> new ResourceNotFoundException("Department not found for ID: " + doctorDto.getDepartmentId()));

        	doctor.setDepartmentId(department.getDepartmentId());
        	doctor.setSpecialty(department.getName()); // <-- Add this line here

doctor.setStatus(doctorDto.getStatus());
        // ☁️ Upload profile photo if available
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(profilePhoto.getBytes(), ObjectUtils.emptyMap());
                doctor.setPhotoUrl(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Photo upload failed", e);
            }
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

}
