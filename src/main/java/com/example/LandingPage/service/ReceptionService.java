package com.example.LandingPage.service;

import com.example.LandingPage.model.Reception;
import com.example.LandingPage.repository.ReceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceptionService {

    @Autowired
    private ReceptionRepository receptionRepository;

    public List<Reception> getAllReceptions() {
        return receptionRepository.findAll();
    }

    public Optional<Reception> getReceptionById(Long id) {
        return receptionRepository.findById(id);
    }

    public Reception saveReception(Reception reception) {
        return receptionRepository.save(reception);
    }

    public void deleteReception(Long id) {
        receptionRepository.deleteById(id);
    }
}