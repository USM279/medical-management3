package com.medical.medicalmanagement.service;

import com.medical.medicalmanagement.dto.PatientDTO;
import com.medical.medicalmanagement.entity.Patient;
import com.medical.medicalmanagement.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // 1. HASTA EKLEME
    public void addPatient(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setName(patientDTO.getName());
        patient.setComplaints(patientDTO.getComplaints());

        Patient savedPatient = patientRepository.save(patient);
        log.info("YENI HASTA: Kayıt oluşturuldu. ID: {}, İsim: {}", savedPatient.getId(), savedPatient.getName());
    }

    // 2. HASTALARI LİSTELEME
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 3. ID'YE GÖRE HASTA BUL (Düzenleme Formu İçin)
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı"));
        return convertToDTO(patient);
    }

    // 4. HASTA GÜNCELLEME
    public void updatePatient(Long id, PatientDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı"));

        patient.setName(dto.getName());
        patient.setComplaints(dto.getComplaints());

        patientRepository.save(patient);
        log.info("HASTA GÜNCELLEME: ID: {} olan hasta bilgileri güncellendi.", id);
    }

    // 5. HASTA SİLME
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
        log.info("HASTA SILINDI: ID'si {} olan hasta sistemden kaldırıldı.", id);
    }

    // Yardımcı Metod: DTO Çevirici
    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setComplaints(patient.getComplaints());
        return dto;
    }
}