package ru.feryafox.carrental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.feryafox.carrental.entities.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;


public interface CarRepository extends JpaRepository<Car, Integer> {
    void deleteById(long id);
        @Query("SELECT DISTINCT c FROM Car c JOIN c.rents r WHERE r.rentStart = :date OR r.rentEnd = :date")
        List<Car> findCarsToClean(@Param("date") LocalDate date);



}
