package ru.feryafox.carrental.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.feryafox.carrental.dto.RegistrationDto;
import ru.feryafox.carrental.services.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService; // Ваш сервис для работы с пользователями

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userEntity", new RegistrationDto());
        return "registration"; // Имя вашего Thymeleaf шаблона
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") @Valid RegistrationDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }

        userService.registerNewAccount(userDto); // Сохранение пользователя в базе данных


        return "redirect:/login?success";
    }
}