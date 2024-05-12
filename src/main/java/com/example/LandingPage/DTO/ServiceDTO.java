package com.example.LandingPage.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private Long id;

    private String name;

    private String description;

    private Long price;
}
