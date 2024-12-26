package ru.feryafox.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarCreateDto {
    private String name;
    private String breed;//breed
 //   private String serialNumber;
    private String diases;//diases
    private BigDecimal price;
}