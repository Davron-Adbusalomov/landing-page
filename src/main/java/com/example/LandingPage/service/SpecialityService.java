package com.example.LandingPage.service;

import com.example.LandingPage.model.Speciality;
import com.example.LandingPage.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialityService {

    @Autowired
    private SpecialityRepository specialityRepository;

    public List<Speciality> getAllSpecialities() {
        return specialityRepository.findAll();
    }

    public Optional<Speciality> getSpecialityById(Long id) {
        return specialityRepository.findById(id);
    }

    public Speciality saveSpeciality(Speciality speciality) {
        return specialityRepository.save(speciality);
    }

    public void deleteSpeciality(Long id) {
        specialityRepository.deleteById(id);
    }
}