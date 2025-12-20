package br.edu.ufape.alugafacil.controllers;


import br.edu.ufape.alugafacil.dtos.user.UserRequest;
import br.edu.ufape.alugafacil.dtos.user.UserResponse;
import br.edu.ufape.alugafacil.exceptions.UserCpfDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserEmailDuplicadoException;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.services.interfaces.IUserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest user) {

        try {
         UserResponse userResponse = userService.saveUser(user);
         return  ResponseEntity.ok(userResponse);

        } catch (UserCpfDuplicadoException | UserEmailDuplicadoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        try {
            UserResponse userResponse = userService.getUserById(id);
            return ResponseEntity.ok(userResponse);
        } catch (UserNotFoundException e) {
           return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody UserRequest user) {
        try {
            UserResponse updated = userService.updateUser(id, user);
            return ResponseEntity.ok(updated);
        } catch (UserNotFoundException | UserCpfDuplicadoException | UserEmailDuplicadoException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            userService.deleteUser(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }
}