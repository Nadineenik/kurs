package ru.feryafox.carrental.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.feryafox.carrental.entities.BookingForm;
import ru.feryafox.carrental.entities.Car;
import ru.feryafox.carrental.entities.UserEntity;
import ru.feryafox.carrental.services.CarService;
import ru.feryafox.carrental.dto.CarDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final CarService carService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UserController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String userIndex(Model model, @AuthenticationPrincipal UserEntity user) {
        model.addAttribute("userRents", carService.getUserRents(user.getId()));
        model.addAttribute("availableCars", carService.getAvailableCars(LocalDate.now(), LocalDate.now().plusMonths(1))); // отображаем автомобили на месяц вперед
        model.addAttribute("bookingForm", new BookingForm()); // Добавляем пустую форму для бронирования
        return "user/index";
    }

    @PostMapping("/") // Обработка бронирования на той же странице
    public String handleBooking(@ModelAttribute("bookingForm") BookingForm bookingForm, Model model, @AuthenticationPrincipal UserEntity user) {
        try {
            if (!carService.isCarAvailable(bookingForm.getCarId(), bookingForm.getStartDate(), bookingForm.getEndDate())) {
                model.addAttribute("error", "Запись на этот день не возможна.");
                model.addAttribute("availableCars", carService.getAvailableCars(LocalDate.now(), LocalDate.now().plusMonths(1)));
                model.addAttribute("userRents", carService.getUserRents(user.getId()));
                return "user/index";
            }
            BigDecimal totalPrice = calculateTotalPrice(bookingForm.getCarId(), bookingForm.getStartDate(), bookingForm.getEndDate());
            carService.createRent(user.getId(), bookingForm.getCarId(), bookingForm.getStartDate(), bookingForm.getEndDate(), totalPrice);
            return "redirect:/user/";
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Неверный формат даты: " + e.getMessage());
            model.addAttribute("availableCars", carService.getAvailableCars(LocalDate.now(), LocalDate.now().plusMonths(1)));
            model.addAttribute("userRents", carService.getUserRents(user.getId()));
            return "user/index";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка бронирования: " + e.getMessage());
            model.addAttribute("availableCars", carService.getAvailableCars(LocalDate.now(), LocalDate.now().plusMonths(1)));
            model.addAttribute("userRents", carService.getUserRents(user.getId()));
            return "user/index";
        }
    }

    private BigDecimal calculateTotalPrice(Long carId, LocalDate startDate, LocalDate endDate){
        Car car = carService.findCar(carId);
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return car.getPrice().multiply(BigDecimal.valueOf(daysBetween));
    }

    @GetMapping("/availableCars")
    public String showAvailableCars(@RequestParam String startDate, @RequestParam String endDate, Model model) {
        try {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            List<CarDto> availableCars = carService.getAvailableCars(start, end);
            model.addAttribute("availableCars", availableCars);
            return "user/availableCars";
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Invalid date format.");
            return "user/availableCars";
        }
    }

    @PostMapping("/deleteRent/{id}")
    public String handleDeleteRent(@PathVariable Long id) {
        carService.deleteRent(id);
        return "redirect:/user/";
    }
}