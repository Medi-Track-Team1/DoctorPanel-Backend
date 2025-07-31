package com.kce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kce.dto.AppointmentDto;

import com.kce.entity.Appointments;
import com.kce.mapper.AppointmentMapper;
import com.kce.repository.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService{

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Override
	public List<AppointmentDto> getAllAppointments() {
		List<Appointments> appointments=appointmentRepository.findAll();
		return appointments.stream().map((appointment)->AppointmentMapper.mapToAppointmentDto(appointment)).collect(Collectors.toList());
	}

	@Override
	public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
	    Appointments appointment = AppointmentMapper.mapToAppointment(appointmentDto);

	    // ✅ Set default status before saving
	    appointment.setStatus("pending");

	    Appointments createdAppointment = null;
	    try {
	        createdAppointment = appointmentRepository.save(appointment);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return AppointmentMapper.mapToAppointmentDto(createdAppointment);
	}


	

	@Override
	public AppointmentDto markCompleted(String appointmentId) {
		Appointments appointment = appointmentRepository.findById(appointmentId)
		        .orElseThrow(() -> new RuntimeException("Appointment not found"));
		    appointment.setStatus("completed");
		    Appointments updated = appointmentRepository.save(appointment);
		    return AppointmentMapper.mapToAppointmentDto(updated); 
	}

	@Override
	public List<AppointmentDto> getAppointmentsByDoctorId(String doctorId) {
	    List<Appointments> appointments = appointmentRepository.findByDoctorId(doctorId);
	    return appointments.stream()
	        .map(AppointmentMapper::mapToAppointmentDto)
	        .collect(Collectors.toList());
	}

	@Override
	public List<AppointmentDto> getAppointmentsByDoctorIdAndStatus(String doctorId, String status) {
	    List<Appointments> filtered = appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
	    return filtered.stream()
	        .map(AppointmentMapper::mapToAppointmentDto)
	        .collect(Collectors.toList());
	}

	@Override
	public List<AppointmentDto> getCompletedAppointmentsByDoctorId(String doctorId) {
	    List<Appointments> completed = appointmentRepository.findByDoctorIdAndStatus(doctorId, "completed");
	    return completed.stream()
	        .map(AppointmentMapper::mapToAppointmentDto)
	        .collect(Collectors.toList());
	}
	@Override
	public AppointmentDto getAppointmentForDoctor(String doctorId, String appointmentId) {
	    Appointments appointment = appointmentRepository.findByAppointmentIdAndDoctorId(appointmentId, doctorId)
	        .orElseThrow(() -> new RuntimeException("Appointment not found for doctorId: " + doctorId));
	    return AppointmentMapper.mapToAppointmentDto(appointment);
	}

	

}
