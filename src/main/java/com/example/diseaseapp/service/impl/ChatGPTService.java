package com.example.diseaseapp.service.impl;

import com.example.diseaseapp.model.MedicalRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatGPTService {

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public String getChatGPTResponse(MedicalRecord medicalRecord) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Construct the prompt from the medical record
        String prompt = constructPromptFromMedicalRecord(medicalRecord);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", new Object[]{new HashMap<String, String>() {{
            put("role", "user");
            put("content", prompt);
        }}});
        requestBody.put("max_tokens", 850);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

        return extractContentFromResponse(response.getBody());
    }

    private String constructPromptFromMedicalRecord(MedicalRecord medicalRecord) {
        return String.format(
                "Patient Medical Record: \n" +
                        "Gender: %s\n" +
                        "Age: %d\n" +
                        "Urea: %.2f\n" +
                        "Creatinine (Cr): %.2f\n" +
                        "HbA1c: %.2f\n" +
                        "Cholesterol (Chol): %.2f\n" +
                        "Triglycerides (TG): %.2f\n" +
                        "HDL: %.2f\n" +
                        "LDL: %.2f\n" +
                        "VLDL: %.2f\n" +
                        "BMI: %.2f\n" +
                        "Please provide input on the chance of the patient having diabetes based on each of the following criteria, don't forget to account for the patient's gender representing different normal values:\n" +
                        "2. Age\n" +
                        "3. Urea\n" +
                        "4. Creatinine (Cr)\n" +
                        "5. HbA1c\n" +
                        "6. Cholesterol (Chol)\n" +
                        "7. Triglycerides (TG)\n" +
                        "8. HDL\n" +
                        "9. LDL\n" +
                        "10. VLDL\n" +
                        "11. BMI",
                medicalRecord.getGender(),
                medicalRecord.getAge(),
                medicalRecord.getUrea(),
                medicalRecord.getCr(),
                medicalRecord.getHba1c(),
                medicalRecord.getChol(),
                medicalRecord.getTg(),
                medicalRecord.getHdl(),
                medicalRecord.getLdl(),
                medicalRecord.getVldl(),
                medicalRecord.getBmi()
        );
    }

    private String extractContentFromResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract content from response", e);
        }
    }
}
