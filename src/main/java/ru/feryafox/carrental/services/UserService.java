package ru.feryafox.carrental.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.feryafox.carrental.dto.RegistrationDto;
import ru.feryafox.carrental.entities.UserEntity;
import ru.feryafox.carrental.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerNewAccount(RegistrationDto registrationDto) {
        // Проверка, существует ли уже пользователь с таким email
        if (userRepository.findByEmail(registrationDto.getEmail()) != null) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        // Создание нового пользователя
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registrationDto.getEmail()); // Email как username
        userEntity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userEntity.setFirstName(registrationDto.getFirstName());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPhone(registrationDto.getPhone());
        userEntity.setRole("ROLE_USER"); // Установка роли по умолчанию

        userRepository.save(userEntity);
    }

    // ... другие методы сервиса, если необходимо
}