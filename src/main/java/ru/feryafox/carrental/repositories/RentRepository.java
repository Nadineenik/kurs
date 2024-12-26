package ru.feryafox.carrental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.feryafox.carrental.entities.Rent;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Integer> {
    List<Rent> findByUserId(Long userId);

    List<Rent> findByCarId(Long id);
    List<Rent> findByCabId(Long id);

}
