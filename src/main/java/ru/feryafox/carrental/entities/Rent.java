package ru.feryafox.carrental.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.Manager;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "rents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "rent_start", nullable = false)
    private LocalDate rentStart;

    @Column(name = "rent_end", nullable = false)
    private LocalDate rentEnd;

    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cab_id")
    private Cab cab;



}