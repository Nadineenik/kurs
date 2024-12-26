package ru.feryafox.carrental.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cabs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

  //  @Column(nullable = false)
   // private String serialNumber;

    @Column(nullable = false)
    private String equipment;

 //   @Column(precision = 10, scale = 2)
   // private BigDecimal price;

    @Column(nullable = false)
    private Boolean booked = false;

    @OneToMany(mappedBy = "cab", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Rent> rents;


    public boolean isAvailable(LocalDate startDate, LocalDate endDate) {
        return !booked && rents.stream().noneMatch(rent -> !rent.getRentEnd().isBefore(startDate) && !rent.getRentStart().isAfter(endDate));
    }
}
