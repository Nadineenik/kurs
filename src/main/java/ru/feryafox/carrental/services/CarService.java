package ru.feryafox.carrental.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.feryafox.carrental.dto.CarCreateDto;
import ru.feryafox.carrental.dto.CarDto;
import ru.feryafox.carrental.entities.Car;
import ru.feryafox.carrental.entities.Rent;
import ru.feryafox.carrental.entities.UserEntity;
import ru.feryafox.carrental.repositories.CarRepository;
import ru.feryafox.carrental.repositories.RentRepository;
import ru.feryafox.carrental.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentRepository rentRepository;


    public CarService(CarRepository carRepository, UserRepository userRepository, RentRepository rentRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.rentRepository = rentRepository;
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public void addCar(CarCreateDto carDto) {
        Car.CarBuilder carBuilder = Car.builder();
        carBuilder.name(carDto.getName());
        carBuilder.breed(carDto.getBreed());
        // carBuilder.serialNumber(carDto.getSerialNumber());
        carBuilder.diases(carDto.getDiases());
        carBuilder.price(carDto.getPrice());
        carBuilder.booked(false);
        carRepository.save(carBuilder.build());
    }

    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public List<CarDto> getAvailableCars(LocalDate startDate, LocalDate endDate) {
        return carRepository.findAll().stream()
                .filter(car -> car.isAvailable(startDate, endDate))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CarDto convertToDto(Car car) {
        return CarDto.builder()
                .id(car.getId())
                .name(car.getName())
                .breed(car.getBreed())
                //.serialNumber(car.getSerialNumber())
                .diases(car.getDiases())
                .price(car.getPrice())
                .booked(car.getBooked())
                .build();
    }


    public void createRent(Long userId, Long carId, LocalDate startDate, LocalDate endDate, BigDecimal totalPrice) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        Car car = carRepository.findById(Math.toIntExact(carId)).orElseThrow();
        Rent rent = new Rent();
        rent.setUser(user);
        rent.setCar(car);
        rent.setRentStart(startDate);
        rent.setRentEnd(endDate);
        rent.setTotalPrice(totalPrice);
        rentRepository.save(rent);
        car.setBooked(true); // Set booked to true after successful booking
        carRepository.save(car);
    }


    public List<Rent> getUserRents(Long userId) {
        return rentRepository.findByUserId(userId);
    }

    public Car findCar(Long carId){
        return carRepository.findById(Math.toIntExact(carId)).orElseThrow();
    }

    public void deleteRent(Long rentId){
        Rent rent = rentRepository.findById(Math.toIntExact(rentId)).orElseThrow();
        rentRepository.delete(rent);
        rent.getCar().setBooked(false);
        carRepository.save(rent.getCar());
    }
    // ... other methods ...
    public boolean isCarAvailable(Long carId, LocalDate startDate, LocalDate endDate) {
        Optional<Car> carOptional = carRepository.findById(Math.toIntExact(carId));
        if (carOptional.isEmpty()) {
            return false; // Автомобиль не найден
        }
        Car car = carOptional.get();

        List<Rent> rents = rentRepository.findByCarId(car.getId());
        for (Rent rent : rents) {
            // Проверка на пересечение дат
            if (startDate.isBefore(rent.getRentEnd()) && endDate.isAfter(rent.getRentStart())) {
                return false; // Автомобиль забронирован на указанный период
            }
        }
        return true; // Автомобиль доступен
    }

    public List<Car> getCarsToClean(LocalDate date) {
        return carRepository.findCarsToClean(date);
    }

    public List<Car> getAllCarsSortedByStatus() {
        List<Car> allCars = carRepository.findAll();
        return allCars.stream()
                .sorted((car1, car2) -> {
                    if (car1.getBooked() == car2.getBooked()) {
                        return car1.getName().compareTo(car2.getName());
                    }
                    return car1.getBooked() ? 1 : -1; // Свободные сначала
                })
                .collect(Collectors.toList());
    }
    public Rent findRentById(Long rentId) {
        return rentRepository.findById(Math.toIntExact(rentId)).orElseThrow();
    }

    public void updateRentDates(Long rentId, String startDate, String endDate) {
        Rent rent = findRentById(rentId);
        LocalDate newStartDate = LocalDate.parse(startDate);
        LocalDate newEndDate = LocalDate.parse(endDate);

        if (newStartDate.isBefore(newEndDate)) {
            rent.setRentStart(newStartDate);
            rent.setRentEnd(newEndDate);
            rentRepository.save(rent);
        } else {
            throw new IllegalArgumentException("Дата начала должна быть до даты окончания.");
        }
    }


}