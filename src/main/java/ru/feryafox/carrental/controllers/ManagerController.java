package ru.feryafox.carrental.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.feryafox.carrental.dto.CarCreateDto;
import ru.feryafox.carrental.dto.CarDto;
import ru.feryafox.carrental.entities.Car;
import ru.feryafox.carrental.services.CarService;

import java.util.List;

@Controller
@RequestMapping("/manager/")
public class ManagerController {
    private final CarService carService;

    public ManagerController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/") // Или другой URL, соответствующий вашему запросу
    public String managerIndex(Model model) {
        model.addAttribute("car", new CarCreateDto()); // Создайте новый объект Car и добавьте его в модель
        model.addAttribute("cars", carService.getAllCars());
        return "manager/index";
    }

    @PostMapping("/")
    public String handleFormSubmission(
            @Valid @ModelAttribute("car") CarCreateDto carCreateDto,
            BindingResult bindingResult,
            Model model) { // Добавьте Model

        if (bindingResult.hasErrors()) {
            // Добавьте объект car обратно в модель, если есть ошибки
            model.addAttribute("cars", carService.getAllCars());
            model.addAttribute("car", new CarCreateDto());
            return "manager/index";
        }
        carService.addCar(carCreateDto);
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("car", new CarCreateDto());
        return "manager/index";
    }

    @PostMapping("/delete/{id}")
    public String handleDelete(
            @PathVariable long id
    ) {
        carService.deleteCar(id);
        return "redirect:/manager/";
    }
}
