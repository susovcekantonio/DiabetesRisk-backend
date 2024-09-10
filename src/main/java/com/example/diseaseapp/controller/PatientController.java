package com.example.diseaseapp.controller;

import com.example.diseaseapp.model.LoginRequest;
import com.example.diseaseapp.model.LoginResponse;
import com.example.diseaseapp.model.Patient;
import com.example.diseaseapp.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/save")
    public String savePatient(@RequestBody Patient patient) {
        patientService.savePatient(patient);
        return "Patient saved successfully";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient and associated medical records deleted successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Patient patient = patientService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (patient != null) {
            return ResponseEntity.ok(new LoginResponse(patient.getId(), "Login successful"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}
