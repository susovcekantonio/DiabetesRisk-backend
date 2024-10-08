package com.example.diseaseapp.controller;

import com.example.diseaseapp.model.MedicalRecord;
import com.example.diseaseapp.model.Patient;
import com.example.diseaseapp.service.MedicalRecordService;
import com.example.diseaseapp.service.PatientService;
import com.example.diseaseapp.service.impl.AzureMLService;
import com.example.diseaseapp.service.impl.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ChatGPTService chatGPTService;

    @Autowired
    private AzureMLService azureMLService;


    @PostMapping("/{id}/medicalrecord/save")
    public ResponseEntity<String> saveMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }


        medicalRecord.setPatient(patient);
        medicalRecordService.saveMedicalRecord(medicalRecord);

        return ResponseEntity.status(HttpStatus.CREATED).body("Medical record saved successfully");
    }

    @GetMapping("/medical-record/{recordId}")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long recordId) {
        MedicalRecord medicalRecord = medicalRecordService.findById(recordId);
        if (medicalRecord == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(medicalRecord);
    }

    @GetMapping("/{id}/medicalrecord")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByPatientId(@PathVariable Long id) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<MedicalRecord> medicalRecords = medicalRecordService.getMedicalRecordsByPatientId(id);
        if (medicalRecords.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(medicalRecords);
    }

    @DeleteMapping("/medicalrecord/{id}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.ok("Medical record deleted successfully");
    }


    @PostMapping("/{id}/medicalrecord/gpt")
    public String getChatGPTResponse(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return "Patient not found";
        }
        medicalRecord.setPatient(patient);

        return chatGPTService.getChatGPTResponse(medicalRecord);
    }

    @PostMapping("/{id}/medicalrecord/azure")
    public String getAzureMLResponse(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return "Patient not found";
        }
        medicalRecord.setPatient(patient);

        return azureMLService.predict(medicalRecord);
    }
}
