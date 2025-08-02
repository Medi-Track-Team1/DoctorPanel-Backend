package com.kce.mapper;

import com.kce.dto.AppointmentDto;
import com.kce.entity.Appointments;

public class AppointmentMapper {

    public static AppointmentDto mapToAppointmentDto(Appointments appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentDto dto = new AppointmentDto();

        // CRITICAL: Map the business logic appointmentId, NOT the MongoDB _id
        dto.setAppointmentId(appointment.getAppointmentId()); // Business ID like "apt_1008"
        dto.setPatientId(appointment.getPatientId());
        dto.setPatientName(appointment.getPatientName());
        dto.setAge(appointment.getAge());
        dto.setPhone(appointment.getPhone());
        dto.setEmail(appointment.getEmail());
        dto.setDepartment(appointment.getDepartment());
        dto.setDoctorId(appointment.getDoctorId());
        dto.setDate(appointment.getDate());
        dto.setTime(appointment.getTime());
        dto.setReason(appointment.getReason());
        dto.setNotes(appointment.getNotes());
        dto.setStatus(appointment.getStatus());

        return dto;
    }

    public static Appointments mapToAppointment(AppointmentDto appointmentDto) {
        if (appointmentDto == null) {
            return null;
        }

        Appointments appointment = new Appointments();

        // Map the business logic appointmentId
        appointment.setAppointmentId(appointmentDto.getAppointmentId()); // Business ID like "apt_1008"
        appointment.setPatientId(appointmentDto.getPatientId());
        appointment.setPatientName(appointmentDto.getPatientName());
        appointment.setAge(appointmentDto.getAge());
        appointment.setPhone(appointmentDto.getPhone());
        appointment.setEmail(appointmentDto.getEmail());
        appointment.setDepartment(appointmentDto.getDepartment());
        appointment.setDoctorId(appointmentDto.getDoctorId());
        appointment.setDate(appointmentDto.getDate());
        appointment.setTime(appointmentDto.getTime());
        appointment.setReason(appointmentDto.getReason());
        appointment.setNotes(appointmentDto.getNotes());
        appointment.setStatus(appointmentDto.getStatus());

        // Note: The MongoDB _id (appointment.getId()) will be auto-generated

        return appointment;
    }
}