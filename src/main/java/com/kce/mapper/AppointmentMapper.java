package com.kce.mapper;

import com.kce.dto.AppointmentDto;
import com.kce.entity.Appointments;

public class AppointmentMapper {
    public static AppointmentDto mapToAppointmentDto(Appointments appointment) {
        return new AppointmentDto(
            appointment.getAppointmentId(),
            appointment.getPatientId(),
            appointment.getPatientName(),
            appointment.getAge(),
            appointment.getPhone(),
            appointment.getEmail(),
            
            appointment.getDepartment(),
            appointment.getDoctorId(),
            appointment.getDate(),
            appointment.getTime(),

            appointment.getReason(),
            appointment.getNotes(),
            appointment.getStatus()
        );
    }

    public static Appointments mapToAppointment(AppointmentDto dto) {
        return new Appointments(
            dto.getAppointmentId(),
            dto.getPatientId(),
            dto.getPatientName(),
            dto.getAge(),
            dto.getPhone(),
            dto.getEmail(),
           
            dto.getDepartment(),
            dto.getDoctorId(),
            dto.getDate(),
            dto.getTime(),

            dto.getReason(),
            dto.getNotes(),
            dto.getStatus()
        );
    }
}
