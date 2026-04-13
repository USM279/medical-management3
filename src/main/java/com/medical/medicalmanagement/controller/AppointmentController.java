package com.medical.medicalmanagement.controller;

import com.medical.medicalmanagement.dto.AppointmentDTO;
import com.medical.medicalmanagement.service.AppointmentService;
import com.medical.medicalmanagement.service.DoctorService;
import com.medical.medicalmanagement.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentController(AppointmentService appointmentService,
                                 DoctorService doctorService,
                                 PatientService patientService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointments";
    }

    @GetMapping("/add")
    public String showAddAppointmentForm(Model model) {
        model.addAttribute("appointment", new AppointmentDTO());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients());
        return "add-appointment";
    }

    @PostMapping("/add")
    public String addAppointment(@ModelAttribute("appointment") AppointmentDTO appointmentDTO) {
        appointmentService.addAppointment(appointmentDTO);
        return "redirect:/appointments";
    }


    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }
}