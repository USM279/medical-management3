package com.medical.medicalmanagement.service;

import com.medical.medicalmanagement.dto.AppointmentDTO;
import com.medical.medicalmanagement.entity.Appointment;
import com.medical.medicalmanagement.entity.Doctor;
import com.medical.medicalmanagement.entity.Patient;
import com.medical.medicalmanagement.repository.AppointmentRepository;
import com.medical.medicalmanagement.repository.DoctorRepository;
import com.medical.medicalmanagement.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public void addAppointment(AppointmentDTO dto) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(dto.getAppointmentDate());

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı"));

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        appointmentRepository.save(appointment);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("NEW APPOINTMENT CREATED: Doctor='{}', Patient='{}', Date='{}', Created by user='{}'",
                doctor.getName(), patient.getName(), dto.getAppointmentDate(), auth.getName());
    }

    public void updateAppointment(Long id, AppointmentDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setAppointmentDate(dto.getAppointmentDate());

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        appointmentRepository.save(appointment);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("APPOINTMENT UPDATED: ID={}, Updated by user='{}'", id, auth.getName());
    }

    public void deleteAppointment(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        appointmentRepository.deleteById(id);
        log.info("APPOINTMENT DELETED: ID={}, Deleted by user='{}'", id, auth.getName());
    }

    public AppointmentDTO getAppointmentById(Long id) {
        Appointment app = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(app.getId());
        dto.setAppointmentDate(app.getAppointmentDate());
        dto.setDoctorId(app.getDoctor().getId());
        dto.setPatientId(app.getPatient().getId());
        dto.setDoctorName(app.getDoctor().getName());
        dto.setPatientName(app.getPatient().getName());
        return dto;
    }

    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(app -> {
            AppointmentDTO dto = new AppointmentDTO();
            dto.setId(app.getId());
            dto.setAppointmentDate(app.getAppointmentDate());
            dto.setDoctorId(app.getDoctor().getId());
            dto.setPatientId(app.getPatient().getId());
            dto.setDoctorName(app.getDoctor().getName());
            dto.setPatientName(app.getPatient().getName());
            return dto;
        }).collect(Collectors.toList());
    }
}