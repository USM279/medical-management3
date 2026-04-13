package com.medical.medicalmanagement.service;

import com.medical.medicalmanagement.dto.AppointmentDTO;
import com.medical.medicalmanagement.entity.Appointment;
import com.medical.medicalmanagement.entity.Doctor;
import com.medical.medicalmanagement.entity.Patient;
import com.medical.medicalmanagement.repository.AppointmentRepository;
import com.medical.medicalmanagement.repository.DoctorRepository;
import com.medical.medicalmanagement.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
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
        log.info("RANDEVU OLUŞTURULDU: Doktor {} -> Hasta {} | Tarih: {}",
                doctor.getName(), patient.getName(), dto.getAppointmentDate());
    }

    // YENİ: SİLME METODU
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
        log.info("RANDEVU IPTAL: ID'si {} olan randevu sistemden silindi.", id);
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