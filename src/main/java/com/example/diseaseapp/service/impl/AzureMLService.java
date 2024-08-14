package com.example.diseaseapp.service.impl;

import com.example.diseaseapp.model.MedicalRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AzureMLService {

    @Value("${azureml.api.url}")
    private String azureMlUrl;

    @Value("${azureml.api.key}")
    private String azureMlApiKey;

    @Autowired
    private ObjectMapper objectMapper;

    public String predict(MedicalRecord medicalRecord) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + azureMlApiKey);

        // Construct the request body
        String requestBody = constructRequestBody(medicalRecord);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(azureMlUrl, HttpMethod.POST, entity, String.class);

        String scoredLabel = extractScoredLabelsFromResponse(response.getBody());

        return generateEvaluationMessage(medicalRecord, scoredLabel);
    }

    private String constructRequestBody(MedicalRecord medicalRecord) {
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("Gender", medicalRecord.getGender());
        inputs.put("AGE", medicalRecord.getAge());
        inputs.put("Urea", medicalRecord.getUrea());
        inputs.put("Cr", medicalRecord.getCr());
        inputs.put("HbA1c", medicalRecord.getHba1c());
        inputs.put("Chol", medicalRecord.getChol());
        inputs.put("TG", medicalRecord.getTg());
        inputs.put("HDL", medicalRecord.getHdl());
        inputs.put("LDL", medicalRecord.getLdl());
        inputs.put("VLDL", medicalRecord.getVldl());
        inputs.put("BMI", medicalRecord.getBmi());
        inputs.put("CLASS", false);

        Map<String, Object> input1 = new HashMap<>();
        input1.put("input1", List.of(inputs));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Inputs", input1);
        requestBody.put("GlobalParameters", new HashMap<>());

        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("Failed to construct Azure ML request body", e);
        }
    }

    private String extractScoredLabelsFromResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("Results").path("WebServiceOutput0").get(0).path("Scored Labels").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract Scored Labels from Azure ML response", e);
        }
    }

    private String generateEvaluationMessage(MedicalRecord medicalRecord, String scoredLabel) {
        StringBuilder evaluationMessage = new StringBuilder();

        evaluationMessage.append(evaluate("BMI", medicalRecord.getBmi(), 18.5, 24.9, 30));
        evaluationMessage.append(evaluate("HbA1c", medicalRecord.getHba1c(), 5.7, 6.4, 7));
        evaluationMessage.append(evaluate("Cholesterol", medicalRecord.getChol(), 200, 239, 240));
        evaluationMessage.append(evaluate("Triglycerides", medicalRecord.getTg(), 150, 199, 200));
        evaluationMessage.append(evaluate("HDL", medicalRecord.getHdl(), 40, 60, 60));
        evaluationMessage.append(evaluate("LDL", medicalRecord.getLdl(), 100, 159, 160));
        evaluationMessage.append(evaluate("VLDL", medicalRecord.getVldl(), 2, 30, 30));
        evaluationMessage.append(evaluate("Urea", medicalRecord.getUrea(), 7, 20, 50));
        evaluationMessage.append(evaluate("Creatinine", medicalRecord.getCr(), 0.6, 1.2, 1.3));

        evaluationMessage.append(String.format("Predicted diabetes status: %s", scoredLabel.equals("true") ? "Positive" : "Negative"));

        return evaluationMessage.toString();
    }

    private String evaluate(String name, double value, double low, double high, double above) {
        if (value < low) {
            return String.format("%s is below average (%.2f). This could indicate a lower risk.\n", name, value);
        } else if (value >= low && value <= high) {
            return String.format("%s is average (%.2f). This indicates a normal range.\n", name, value);
        } else {
            return String.format("%s is above average (%.2f). This could indicate a higher risk.\n", name, value);
        }
    }
}


