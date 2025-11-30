package com.example.tubesrpl.service;

import com.example.tubesrpl.exception.ResourceNotFoundException;
import com.example.tubesrpl.model.Pengguna;
import com.example.tubesrpl.repository.PenggunaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PenggunaService {

    private final PenggunaRepository penggunaRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Pengguna> findAll() {
        return penggunaRepository.findAll();
    }

    public Pengguna findById(Integer id) {
        return penggunaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pengguna not found with id: " + id));
    }

    public Pengguna findByUsername(String username) {
        return penggunaRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Pengguna not found with username: " + username));
    }

    public Pengguna login(String username, String password) {
        Optional<Pengguna> userOptional = penggunaRepository.findByUsername(username);
        
        if (userOptional.isEmpty()) {
            return null;
        }

        Pengguna user = userOptional.get();

        // Check password matches hash
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        
        return null;
    }

    public Pengguna save(Pengguna pengguna) {
        // Check username uniqueness only for new users
        if (pengguna.getIdPengguna() == null && penggunaRepository.existsByUsername(pengguna.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        // Encode password if not already encoded
        if (pengguna.getPassword() != null && 
            !pengguna.getPassword().startsWith("$2a$") && 
            !pengguna.getPassword().startsWith("$2y$") &&
            !pengguna.getPassword().startsWith("$2b$")) {
            pengguna.setPassword(passwordEncoder.encode(pengguna.getPassword()));
        }
        return penggunaRepository.save(pengguna);
    }

    public void deleteById(Integer id) {
        if (penggunaRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Pengguna not found with id: " + id);
        }
        penggunaRepository.deleteById(id);
    }

    public long count() {
        return penggunaRepository.count();
    }

    public long countByPeran(String peran) {
        return penggunaRepository.countByPeran(peran);
    }
}
