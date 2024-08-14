package com.example.diseaseapp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class LoginResponse {
    private Long id;
    private String message;

    public LoginResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }
}

