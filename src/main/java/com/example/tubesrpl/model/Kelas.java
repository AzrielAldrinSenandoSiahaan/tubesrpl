package com.example.tubesrpl.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Kelas {
    private Long idKelas;
    private String namaKelas;
    private String kodeKelas;
    private Long idMatkul;
    private String namaMatkul;
}
