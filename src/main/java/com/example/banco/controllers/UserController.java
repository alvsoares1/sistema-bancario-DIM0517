package com.example.banco.controllers;

import com.example.banco.entities.User;
import com.example.banco.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        User registeredUser = userService.registerUser(user.getNumUser());
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/{numUser}")
    public ResponseEntity<User> getUserByNumUser(@PathVariable String numUser) {
        User user = userService.getUserByNumUser(numUser);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/debit")
    public ResponseEntity<User> debitUser(@RequestBody User user){
        var debitedUser = userService.debitUser(user.getNumUser(),user.getSaldo());
        return ResponseEntity.ok(debitedUser);
    }
}
