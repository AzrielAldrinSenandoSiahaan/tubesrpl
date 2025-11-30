package com.example.tubesrpl.repository;

import com.example.tubesrpl.model.MataKuliah;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MataKuliahRepository {

    private final JdbcTemplate jdbcTemplate;

    public MataKuliahRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Ambil semua mata kuliah
    public List<MataKuliah> findAll() {
        String sql = "SELECT id_matkul, kode_matkul, nama_matkul, sks FROM mata_kuliah";
        return jdbcTemplate.query(sql, this::mapRowToMataKuliah);
    }

    // Cari mata kuliah berdasarkan ID
    public MataKuliah findById(Long idMatkul) {
        String sql = "SELECT id_matkul, kode_matkul, nama_matkul, sks FROM mata_kuliah WHERE id_matkul = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMataKuliah, idMatkul);
    }

    // Validasi kode mata kuliah unik
    public boolean existsByKodeMatkul(String kodeMatkul) {
        String sql = "SELECT 1 FROM mata_kuliah WHERE kode_matkul = ?";
        return !jdbcTemplate.queryForList(sql, Integer.class, kodeMatkul).isEmpty();
    }

    // Simpan mata kuliah baru
    public void save(String kodeMatkul, String namaMatkul, Integer sks) {
        String sql = "INSERT INTO mata_kuliah (kode_matkul, nama_matkul, sks) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, kodeMatkul, namaMatkul, sks != null ? sks : 3); // default sks=3
    }

    // Update mata kuliah
    public void update(Long idMatkul, String kodeMatkul, String namaMatkul, Integer sks) {
        String sql = "UPDATE mata_kuliah SET kode_matkul = ?, nama_matkul = ?, sks = ? WHERE id_matkul = ?";
        jdbcTemplate.update(sql, kodeMatkul, namaMatkul, sks != null ? sks : 3, idMatkul);
    }

    // Hapus mata kuliah
    public void deleteById(Long idMatkul) {
        String sql = "DELETE FROM mata_kuliah WHERE id_matkul = ?";
        jdbcTemplate.update(sql, idMatkul);
    }

    // Mapping ResultSet ke objek MataKuliah
    private MataKuliah mapRowToMataKuliah(ResultSet rs, int rowNum) throws SQLException {
        MataKuliah mk = new MataKuliah();
        mk.setIdMatkul(rs.getLong("id_matkul"));
        mk.setKodeMatkul(rs.getString("kode_matkul"));
        mk.setNamaMatkul(rs.getString("nama_matkul"));
        mk.setSks(rs.getInt("sks"));
        return mk;
    }
}