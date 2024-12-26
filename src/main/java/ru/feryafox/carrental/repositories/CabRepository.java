package ru.feryafox.carrental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.feryafox.carrental.entities.Cab;
import ru.feryafox.carrental.entities.Car;

import java.time.LocalDate;
import java.util.List;


public interface CabRepository extends JpaRepository<Cab, Integer> {
    void deleteById(long id);
        @Query("SELECT DISTINCT c FROM Cab c JOIN c.rents r WHERE r.rentStart = :date OR r.rentEnd = :date")
        List<Cab> findCabsToClean(@Param("date") LocalDate date);



}
