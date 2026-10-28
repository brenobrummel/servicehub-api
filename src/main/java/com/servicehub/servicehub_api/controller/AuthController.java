package com.servicehub.servicehub_api.controller;

import com.servicehub.servicehub_api.model.User;
import com.servicehub.servicehub_api.repository.UserRepository;
import com.servicehub.servicehub_api.security.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints de Login e Registro")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Estrutura rápida para receber e-mail e senha no login
    public record AuthRequest(String email, String password) {}

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthRequest request) {
        // O Spring Security tenta autenticar com o e-mail e a senha puros
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        
        // Se a senha estiver correta, busca o usuário no banco e gera o token
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        
        return Map.of("token", token);
    }
    
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        // Criptografa a senha antes de salvar no banco de dados
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}