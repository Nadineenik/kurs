package ru.feryafox.carrental.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.feryafox.carrental.entities.UserEntity;
import ru.feryafox.carrental.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // Изменено на email
        UserEntity userEntity = userRepository.findByEmail(email); // Изменено на email
        if (userEntity == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return userEntity;
    }
}