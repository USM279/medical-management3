package com.medical.medicalmanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalDateTime appointmentDate;
    private String description;


    private String doctorName;
    private String patientName;
    private Long doctorId;
    private Long patientId;
}