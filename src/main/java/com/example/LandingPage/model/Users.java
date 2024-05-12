package com.example.LandingPage.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String username;

    private String password;

    private String phone;

    private String education;

    private String achievements;

    private String telegram;

    private byte[] profileImage;

    @OneToMany(mappedBy = "users")
    private List<Speciality> specialities;

    @OneToMany(mappedBy = "users")
    private List<Language> languages;

    @OneToMany(mappedBy = "users")
    private List<Experience> experience;

    @OneToMany(mappedBy = "users")
    private List<Services> services;

    @OneToMany(mappedBy = "users")
    private List<Reception> reception;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
