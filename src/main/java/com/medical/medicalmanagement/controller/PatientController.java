package com.medical.medicalmanagement.controller;

import com.medical.medicalmanagement.dto.PatientDTO;
import com.medical.medicalmanagement.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public String listPatients(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "patients";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new PatientDTO());
        return "add-patient";
    }

    @PostMapping("/add")
    public String addPatient(@ModelAttribute("patient") PatientDTO patientDTO) {
        patientService.addPatient(patientDTO);
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientById(id));
        return "add-patient";
    }

    @PostMapping("/edit/{id}")
    public String updatePatient(@PathVariable Long id, @ModelAttribute("patient") PatientDTO patientDTO) {
        patientService.updatePatient(id, patientDTO);
        return "redirect:/patients";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }
}