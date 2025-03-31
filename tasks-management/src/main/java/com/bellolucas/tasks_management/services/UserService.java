package com.bellolucas.tasks_management.services;

import com.bellolucas.tasks_management.dto.user.CreateUserDTO;
import com.bellolucas.tasks_management.dto.user.UpdateUserDTO;
import com.bellolucas.tasks_management.entities.user.User;
import com.bellolucas.tasks_management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(CreateUserDTO data) {
        String encodedPassword = passwordEncoder.encode(data.password());

        User user = new User(data, encodedPassword);
        return userRepository.save(user);
    }

    public User updateUser(Long id, UpdateUserDTO data) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (data.firstName() != null) {
            user.setFirstName(data.firstName());
        }

        if (data.lastName() != null) {
            user.setLastName(data.lastName());
        }

        if (data.email() != null) {
            user.setEmail(data.email());
        }

        if (data.password() != null) {
            String encodedPassword = passwordEncoder.encode(data.password());
            user.setPassword(encodedPassword);
        }

        return userRepository.save(user);
    }

    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }
}
