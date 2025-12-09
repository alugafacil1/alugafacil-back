package br.edu.ufape.alugafacil.controllers;

import br.edu.ufape.alugafacil.dto.UserDto;
import br.edu.ufape.alugafacil.exceptions.UserNotFoundException;
import br.edu.ufape.alugafacil.models.User;
import br.edu.ufape.alugafacil.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto user) {
       // User created = userService.saveUser(user);
        return ResponseEntity.ok(null);
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
       // return ResponseEntity.ok(userService.getUserById(id));
        return null;
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
      //  User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(null);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        //userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}