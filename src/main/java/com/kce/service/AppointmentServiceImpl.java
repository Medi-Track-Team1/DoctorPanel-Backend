package com.kce.service;

import com.kce.dto.AppointmentDto;
import com.kce.entity.Appointments;
import com.kce.repository.AppointmentRepository;
import com.kce.mapper.AppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Override
	public List<AppointmentDto> getAllAppointments() {
		List<Appointments> appointments = appointmentRepository.findAll();
		return appointments.stream()
				.map(AppointmentMapper::mapToAppointmentDto)
				.collect(Collectors.toList());
	}

	@Override
	public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
		Appointments appointment = AppointmentMapper.mapToAppointment(appointmentDto);
		Appointments savedAppointment = appointmentRepository.save(appointment);
		return AppointmentMapper.mapToAppointmentDto(savedAppointment);
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
		List<Appointments> appointments = appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
		return appointments.stream()
				.map(AppointmentMapper::mapToAppointmentDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<AppointmentDto> getCompletedAppointmentsByDoctorId(String doctorId) {
		return getAppointmentsByDoctorIdAndStatus(doctorId, "completed");
	}

	@Override
	public AppointmentDto markCompleted(String appointmentId) {
		System.out.println("Marking appointment as completed: " + appointmentId);

		// Find by business logic appointmentId
		Appointments appointment = appointmentRepository.findByAppointmentId(appointmentId)
				.orElseThrow(() -> new RuntimeException("Appointment not found with appointmentId: " + appointmentId));

		appointment.setStatus("completed");
		Appointments savedAppointment = appointmentRepository.save(appointment);

		System.out.println("Successfully marked appointment " + appointmentId + " as completed");

		return AppointmentMapper.mapToAppointmentDto(savedAppointment);
	}

	@Override
	public AppointmentDto getAppointmentForDoctor(String doctorId, String appointmentId) {
		System.out.println("=== APPOINTMENT SERVICE DEBUG ===");
		System.out.println("Looking for appointment with:");
		System.out.println("- appointmentId: " + appointmentId);
		System.out.println("- doctorId: " + doctorId);

		// Find by business logic appointmentId and doctorId
		Appointments appointment = appointmentRepository.findByAppointmentIdAndDoctorId(appointmentId, doctorId)
				.orElseThrow(() -> new RuntimeException("Appointment not found with appointmentId: " + appointmentId + " and doctorId: " + doctorId));

		System.out.println("Found appointment:");
		System.out.println("- MongoDB _id: " + appointment.getId());
		System.out.println("- Business appointmentId: " + appointment.getAppointmentId());
		System.out.println("- Patient ID: " + appointment.getPatientId());
		System.out.println("- Patient Name: " + appointment.getPatientName());

		// Convert to DTO
		AppointmentDto dto = AppointmentMapper.mapToAppointmentDto(appointment);

		System.out.println("Returning DTO with appointmentId: " + dto.getAppointmentId());
		System.out.println("=== END APPOINTMENT SERVICE DEBUG ===");

		return dto;
	}
}