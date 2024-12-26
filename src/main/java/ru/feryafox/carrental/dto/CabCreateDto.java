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
public class CabCreateDto {
    private String name;
    private String description;//breed
 //   private String serialNumber;
    private String equipment;//diases
    //private BigDecimal price;
}