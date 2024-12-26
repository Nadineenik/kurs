package ru.feryafox.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private Long id;

    private String name;

    private String breed;//breed

   // private String serialNumber;

    private String diases;

    private BigDecimal price;

    private boolean booked;
}