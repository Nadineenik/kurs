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
public class CabDto {
    private Long id;

    private String name;

    private String description;

   // private String serialNumber;

    private String equipment;

    //private BigDecimal price;

    private boolean booked;
}