package com.medical.medicalmanagement.service;

import com.medical.medicalmanagement.dto.DoctorDTO;
import com.medical.medicalmanagement.entity.Doctor;
import com.medical.medicalmanagement.repository.DoctorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final String uploadDir = "src/main/resources/static/images/";

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // 1. DOKTOR EKLEME
    public void addDoctor(DoctorDTO doctorDTO, MultipartFile imageFile) throws IOException {
        Doctor doctor = new Doctor();
        doctor.setName(doctorDTO.getName());
        doctor.setSpecialty(doctorDTO.getSpecialty());

        handleImageUpload(doctor, imageFile);

        Doctor savedDoctor = doctorRepository.save(doctor);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("NEW DOCTOR CREATED: ID={}, Name='{}', Created by user='{}'",
                savedDoctor.getId(), savedDoctor.getName(), auth.getName());
    }

    // 2. DOKTORLARI LİSTELEME
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 3. ID'YE GÖRE DOKTOR BUL (Düzenleme Formu İçin)
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
        return convertToDTO(doctor);
    }

    // 4. DOKTOR GÜNCELLEME
    public void updateDoctor(Long id, DoctorDTO dto, MultipartFile imageFile) throws IOException {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));

        doctor.setName(dto.getName());
        doctor.setSpecialty(dto.getSpecialty());

        if (imageFile != null && !imageFile.isEmpty()) {
            handleImageUpload(doctor, imageFile);
        }

        doctorRepository.save(doctor);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("DOCTOR UPDATED: ID={}, Updated by user='{}'", id, auth.getName());
    }

    // 5. DOKTOR SİLME
    public void deleteDoctor(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        doctorRepository.deleteById(id);
        log.info("DOCTOR DELETED: ID={}, Deleted by user='{}'", id, auth.getName());
    }

    // Yardımcı Metod: Resim Yükleme
    private void handleImageUpload(Doctor doctor, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());
            doctor.setImagePath("/images/" + fileName);
        }
    }

    // Yardımcı Metod: DTO Çevirici
    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setImagePath(doctor.getImagePath());
        return dto;
    }
}