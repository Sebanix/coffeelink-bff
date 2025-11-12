package com.coffeelink.bff.controller;

import com.coffeelink.bff.dto.AuthResponse;
import com.coffeelink.bff.dto.LoginRequest;
import com.coffeelink.bff.dto.RegisterRequest;
import com.coffeelink.bff.model.User;
import com.coffeelink.bff.repository.UserRepository;
import com.coffeelink.bff.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token, user.getRol()));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Error: El email ya está en uso.");
        }

        String password = registerRequest.getPassword();
        if (!password.matches(".*[a-z].*")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña debe tener al menos una minúscula.");
        }
        if (!password.matches(".*[A-Z].*")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña debe tener al menos una mayúscula.");
        }
        if (!password.matches(".*[0-9].*")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña debe tener al menos un número.");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,./?].*")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña debe tener al menos un símbolo.");
        }

        User newUser = new User();
        newUser.setNombre(registerRequest.getNombre());
        newUser.setEmail(registerRequest.getEmail());

        newUser.setPasswordHash(passwordEncoder.encode(password));

        newUser.setRol("cliente");

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("¡Usuario registrado con éxito!");
    }
}