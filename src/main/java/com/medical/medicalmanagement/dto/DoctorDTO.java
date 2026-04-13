package com.medical.medicalmanagement.dto;

import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialty;
    private String imagePath;
}