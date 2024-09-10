package com.example.diseaseapp.service;

import com.example.diseaseapp.model.MedicalRecord;

import java.util.List;


public interface MedicalRecordService {
    MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord);
    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId);
    public MedicalRecord findById(Long id);

    void deleteMedicalRecord(Long recordId);
}
