package com.example.LandingPage.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String start_from;

    private String end_to;

    private String company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
}
