package ru.feryafox.carrental.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.feryafox.carrental.dto.CabCreateDto;
import ru.feryafox.carrental.dto.CarCreateDto;
import ru.feryafox.carrental.services.CabService;
import ru.feryafox.carrental.services.CarService;

@Controller
@RequestMapping("/header")
public class HeaderController {
    private final CabService cabService;

    public HeaderController(CabService cabService) {
        this.cabService = cabService;
    }

    @GetMapping("/") // Или другой URL, соответствующий вашему запросу
    public String headerIndex(Model model) {
        model.addAttribute("cab", new CabCreateDto()); // Создайте новый объект Car и добавьте его в модель
        model.addAttribute("cabs", cabService.getAllCabs());
        return "header/index";
    }

    @PostMapping("/")
    public String handleFormSubmission(
            @Valid @ModelAttribute("cab") CabCreateDto cabCreateDto,
            BindingResult bindingResult,
            Model model) { // Добавьте Model

        if (bindingResult.hasErrors()) {
            // Добавьте объект car обратно в модель, если есть ошибки
            model.addAttribute("cabs", cabService.getAllCabs());
            model.addAttribute("cab", new CabCreateDto());
            return "header/index";
        }

        cabService.addCab(cabCreateDto);
        model.addAttribute("cabs", cabService.getAllCabs());
        model.addAttribute("cab", new CabCreateDto());
        return "header/index";
    }

    @PostMapping("/delete/{id}")
    public String handleDelete(
            @PathVariable long id
    ) {
        cabService.deleteCab(id);
        return "redirect:/header/";
    }
}
