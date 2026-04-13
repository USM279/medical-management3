package com.medical.medicalmanagement.controller;

import com.medical.medicalmanagement.dto.DoctorDTO;
import com.medical.medicalmanagement.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "doctors";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("doctor", new DoctorDTO());
        return "add-doctor";
    }

    @PostMapping("/add")
    public String addDoctor(@ModelAttribute("doctor") DoctorDTO doctorDTO,
                            @RequestParam("image") MultipartFile imageFile) throws IOException {
        doctorService.addDoctor(doctorDTO, imageFile);
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("doctor", doctorService.getDoctorById(id));
        return "add-doctor";
    }

    @PostMapping("/edit/{id}")
    public String updateDoctor(@PathVariable Long id,
                               @ModelAttribute("doctor") DoctorDTO doctorDTO,
                               @RequestParam("image") MultipartFile imageFile) throws IOException {
        doctorService.updateDoctor(id, doctorDTO, imageFile);
        return "redirect:/doctors";
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/doctors";
    }
}