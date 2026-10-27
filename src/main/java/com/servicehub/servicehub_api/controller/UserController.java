package com.servicehub.controller;
import com.servicehub.model.User;
import com.servicehub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "CRUD de usuários da plataforma")
public class UserController {
    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar Usuário", description = "Cadastra um novo usuário no banco")
    public User create(@RequestBody User user) { return service.create(user); }

    @GetMapping
    @Operation(summary = "Listar Usuários", description = "Retorna todos os usuários")
    public List<User> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um usuário específico")
    public User findById(@PathVariable Long id) { return service.findById(id); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar Usuário", description = "Remove o usuário pelo ID")
    public void delete(@PathVariable Long id) { service.delete(id); }
}