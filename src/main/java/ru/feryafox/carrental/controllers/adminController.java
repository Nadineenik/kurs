package ru.feryafox.carrental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.feryafox.carrental.entities.Car;
import ru.feryafox.carrental.entities.Rent;
import ru.feryafox.carrental.services.CarService;

import java.util.List;



import ru.feryafox.carrental.services.CarService; // Замените путь на ваш пакет
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


@Controller
@RequestMapping("/admin")
public class adminController {
    private final CarService carService;

    public adminController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String showAllCars(Model model) {
        // Получаем список всех машин, сначала свободные, потом забронированные
        List<Car> allCars = carService.getAllCarsSortedByStatus();
        model.addAttribute("cars", allCars);
        return "admin/animal"; // Страница отображения списка машин
    }

    @PostMapping("/deleteRent/{rentId}")
    public String deleteRent(@PathVariable Long rentId) {
        carService.deleteRent(rentId);
        return "redirect:/admin/";
    }

    @GetMapping("/editRent/{rentId}")
    public String showEditRentForm(@PathVariable Long rentId, Model model) {
        Rent rent = carService.findRentById(rentId);
        model.addAttribute("rent", rent);
        return "admin/editRent"; // Страница для редактирования записи
    }

    @PostMapping("/editRent/{rentId}")
    public String editRent(@PathVariable Long rentId,
                           @RequestParam("startDate") String startDate,
                           @RequestParam("endDate") String endDate,
                           Model model) {
        try {
            carService.updateRentDates(rentId, startDate, endDate);
            return "redirect:/admin/";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка обновления записи: " + e.getMessage());
            Rent rent = carService.findRentById(rentId);
            model.addAttribute("rent", rent);
            return "admin/editRent";
        }
    }

}
