package com.example.tubesrpl.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MataKuliah {
    private Long idMatkul;
    private String kodeMatkul;
    private String namaMatkul;
    private Integer sks;
}