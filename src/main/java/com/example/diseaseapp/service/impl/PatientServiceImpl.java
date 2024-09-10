package com.example.diseaseapp.service.impl;

import com.example.diseaseapp.model.MedicalRecord;
import com.example.diseaseapp.model.Patient;
import com.example.diseaseapp.repository.MedicalRecordRepository;
import com.example.diseaseapp.repository.PatientRepository;
import com.example.diseaseapp.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient findById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.orElse(null);
    }

    @Override
    public void deletePatient(Long id) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientId(id);
        medicalRecordRepository.deleteAll(records);

        patientRepository.deleteById(id);
    }

    @Override
    public Patient authenticate(String email, String password) {
        return patientRepository.findByEmail(email)
                .filter(patient -> patient.getPassword().equals(password))
                .orElse(null);
    }
}
