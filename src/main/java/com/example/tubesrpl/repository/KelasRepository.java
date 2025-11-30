package com.example.tubesrpl.repository;

import com.example.tubesrpl.model.Kelas;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class KelasRepository {

    private final JdbcTemplate jdbcTemplate;

    public KelasRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Kelas mapRowToKelas(ResultSet rs, int rowNum) throws SQLException {
        Kelas k = new Kelas();
        k.setIdKelas(rs.getLong("id_kelas"));
        k.setNamaKelas(rs.getString("nama_kelas"));
        k.setKodeKelas(rs.getString("kode_kelas"));
        k.setIdMatkul(rs.getLong("id_matkul"));
        k.setNamaMatkul(rs.getString("nama_matkul"));
        return k;
    }

    public List<Kelas> findAllWithMatkul() {
        String sql = """
            SELECT k.id_kelas, k.nama_kelas, k.kode_kelas, k.id_matkul, mk.nama_matkul
            FROM kelas k
            LEFT JOIN mata_kuliah mk ON k.id_matkul = mk.id_matkul
            """;
        return jdbcTemplate.query(sql, this::mapRowToKelas);
    }

    // Validasi kode kelas unik (kecuali milik diri sendiri)
    public boolean existsByKodeKelasAndIdNot(String kodeKelas, Long idKelas) {
        String sql = "SELECT 1 FROM kelas WHERE kode_kelas = ? AND id_kelas != ?";
        return !jdbcTemplate.queryForList(sql, Integer.class, kodeKelas, idKelas).isEmpty();
    }
    
    public boolean existsByKodeKelas(String kodeKelas) {
        String sql = "SELECT 1 FROM kelas WHERE kode_kelas = ?";
        return !jdbcTemplate.queryForList(sql, Integer.class, kodeKelas).isEmpty();
    }

    public void save(String namaKelas, String kodeKelas, Long idMatkul) {
        String sql = "INSERT INTO kelas (nama_kelas, kode_kelas, id_matkul) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, namaKelas, kodeKelas, idMatkul);
    }

    public void update(Long idKelas, String namaKelas, String kodeKelas, Long idMatkul) {
        String sql = "UPDATE kelas SET nama_kelas = ?, kode_kelas = ?, id_matkul = ? WHERE id_kelas = ?";
        jdbcTemplate.update(sql, namaKelas, kodeKelas, idMatkul, idKelas);
    }

    public void deleteById(Long idKelas) {
        String sql = "DELETE FROM kelas WHERE id_kelas = ?";
        jdbcTemplate.update(sql, idKelas);
    }

    public List<Kelas> findByKeyword(String keyword) {
        String sql = """
            SELECT k.id_kelas, k.nama_kelas, k.kode_kelas, k.id_matkul, mk.nama_matkul
            FROM kelas k
            LEFT JOIN mata_kuliah mk ON k.id_matkul = mk.id_matkul
            WHERE LOWER(k.nama_kelas) LIKE LOWER(?)
               OR LOWER(k.kode_kelas) LIKE LOWER(?)
               OR LOWER(mk.nama_matkul) LIKE LOWER(?)
            """;
        String like = "%" + keyword + "%";
        return jdbcTemplate.query(sql, this::mapRowToKelas, like, like, like);
    }
}