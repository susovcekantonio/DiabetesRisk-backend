package com.example.diseaseapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_sequence")
    @SequenceGenerator(name = "record_sequence", allocationSize = 1)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonBackReference
    private Patient patient;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private int age;

    @Column(name = "urea")
    private double urea;

    @Column(name = "cr")
    private double cr;

    @Column(name = "hba1c")
    private double hba1c;

    @Column(name = "chol")
    private double chol;

    @Column(name = "tg")
    private double tg;

    @Column(name = "hdl")
    private double hdl;

    @Column(name = "ldl")
    private double ldl;

    @Column(name = "vldl")
    private double vldl;

    @Column(name = "bmi")
    private double bmi;


}