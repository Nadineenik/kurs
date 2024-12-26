package ru.feryafox.carrental.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.feryafox.carrental.dto.CabCreateDto;
import ru.feryafox.carrental.dto.CabDto;
import ru.feryafox.carrental.entities.Cab;
import ru.feryafox.carrental.entities.Rent;
import ru.feryafox.carrental.entities.UserEntity;
import ru.feryafox.carrental.repositories.CabRepository;
import ru.feryafox.carrental.repositories.RentRepository;
import ru.feryafox.carrental.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class CabService {
    private final CabRepository cabRepository;
    private final UserRepository userRepository;
    private final RentRepository rentRepository;


    public CabService(CabRepository cabRepository, UserRepository userRepository, RentRepository rentRepository) {
        this.cabRepository = cabRepository;
        this.userRepository = userRepository;
        this.rentRepository = rentRepository;
    }

    public void deleteCab(Long id) {
        cabRepository.deleteById(id);
    }

    public void addCab(CabCreateDto cabDto) {
        Cab.CabBuilder cabBuilder = Cab.builder();
        cabBuilder.name(cabDto.getName());
        cabBuilder.description(cabDto.getDescription());
        // carBuilder.serialNumber(carDto.getSerialNumber());
        cabBuilder.equipment(cabDto.getEquipment());
       // carBuilder.price(carDto.getPrice());
        cabBuilder.booked(false);
        cabRepository.save(cabBuilder.build());
    }

    public List<CabDto> getAllCabs() {
        return cabRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public List<CabDto> getAvailableCabs(LocalDate startDate, LocalDate endDate) {
        return cabRepository.findAll().stream()
                .filter(cab -> cab.isAvailable(startDate, endDate))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CabDto convertToDto(Cab cab) {
        return CabDto.builder()
                .id(cab.getId())
                .name(cab.getName())
                .description(cab.getDescription())
                //.serialNumber(car.getSerialNumber())
                .equipment(cab.getEquipment())
                //.price(car.getPrice())
                .booked(cab.getBooked())
                .build();
    }


    public void createRent(Long userId, Long cabId, LocalDate startDate, LocalDate endDate) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        Cab cab = cabRepository.findById(Math.toIntExact(cabId)).orElseThrow();
        Rent rent = new Rent();
        rent.setUser(user);
        rent.setCab(cab);
        rent.setRentStart(startDate);
        rent.setRentEnd(endDate);
       // rent.setTotalPrice(totalPrice);
        rentRepository.save(rent);
        cab.setBooked(true); // Set booked to true after successful booking
        cabRepository.save(cab);
    }


    public List<Rent> getUserRents(Long userId) {
        return rentRepository.findByUserId(userId);
    }

    public Cab findCab(Long cabId){
        return cabRepository.findById(Math.toIntExact(cabId)).orElseThrow();
    }

    public void deleteRent(Long rentId){
        Rent rent = rentRepository.findById(Math.toIntExact(rentId)).orElseThrow();
        rentRepository.delete(rent);
        rent.getCar().setBooked(false);
        cabRepository.save(rent.getCab());
    }
    // ... other methods ...
    public boolean isCarAvailable(Long cabId, LocalDate startDate, LocalDate endDate) {

        Optional<Cab> cabOptional = cabRepository.findById(Math.toIntExact(cabId));
        if (cabOptional.isEmpty()) {
            return false; // Автомобиль не найден
        }
        Cab cab = cabOptional.get();

        List<Rent> rents = rentRepository.findByCabId(cab.getId());
        for (Rent rent : rents) {
            // Проверка на пересечение дат
            if (startDate.isBefore(rent.getRentEnd()) && endDate.isAfter(rent.getRentStart())) {
                return false; // Автомобиль забронирован на указанный период
            }
        }
        return true; // Автомобиль доступен
    }

    public List<Cab> getCabsToClean(LocalDate date) {
        return cabRepository.findCabsToClean(date);
    }

    public List<Cab> getAllCabsSortedByStatus() {
        List<Cab> allCabs = cabRepository.findAll();
        return allCabs.stream()
                .sorted((cab1, cab2) -> {
                    if (cab1.getBooked() == cab2.getBooked()) {
                        return cab1.getName().compareTo(cab2.getName());
                    }
                    return cab1.getBooked() ? 1 : -1; // Свободные сначала
                })
                .collect(Collectors.toList());
    }

}