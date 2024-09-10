package com.example.diseaseapp.service.impl;

import com.example.diseaseapp.model.MedicalRecord;
import com.example.diseaseapp.repository.MedicalRecordRepository;
import com.example.diseaseapp.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Override
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public MedicalRecord findById(Long id) {
        return medicalRecordRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteMedicalRecord(Long recordId) {
        medicalRecordRepository.deleteById(recordId);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }
}
