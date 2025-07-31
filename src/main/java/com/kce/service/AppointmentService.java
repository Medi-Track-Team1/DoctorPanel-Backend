package com.kce.service;

import java.util.List;

import com.kce.dto.AppointmentDto;

public interface AppointmentService {

    List<AppointmentDto> getAllAppointments();

    AppointmentDto createAppointment(AppointmentDto appointmentDto);

    List<AppointmentDto> getAppointmentsByDoctorId(String doctorId);

    List<AppointmentDto> getAppointmentsByDoctorIdAndStatus(String doctorId, String status);

    List<AppointmentDto> getCompletedAppointmentsByDoctorId(String doctorId);

    AppointmentDto markCompleted(String appointmentId);

    // ✅ New method to support GET /doctor/{doctorId}/appointments/{appointmentId}
    AppointmentDto getAppointmentForDoctor(String doctorId, String appointmentId);
}
