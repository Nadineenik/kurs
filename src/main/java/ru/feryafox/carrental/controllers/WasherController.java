package ru.feryafox.carrental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.feryafox.carrental.entities.Cab;
import ru.feryafox.carrental.entities.Car;
import ru.feryafox.carrental.services.CabService;
import ru.feryafox.carrental.services.CarService;

import java.util.List;

@Controller
@RequestMapping("/washer")
public class WasherController {
    private final CabService cabService;

    public WasherController(CabService cabService) {
        this.cabService = cabService;
    }


    @GetMapping("/")
    public String showAllCabs(Model model) {
        // Получаем список всех машин, сначала свободные, потом забронированные
        List<Cab> allCabs = cabService.getAllCabsSortedByStatus();
        model.addAttribute("cabs", allCabs);
        return "washer/carList"; // Страница отображения списка машин
    }
}
