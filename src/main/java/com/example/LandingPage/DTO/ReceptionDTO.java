package com.example.LandingPage.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionDTO {
    private Long id;

    private String address;

    private String target;

    private String work_time;

    private Long price;
}
