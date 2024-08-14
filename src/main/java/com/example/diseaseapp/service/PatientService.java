package com.example.diseaseapp.service;

import com.example.diseaseapp.model.Patient;


public interface PatientService {
    Patient savePatient(Patient patient);
    Patient findById(Long id);
    Patient authenticate(String email, String password);
}
