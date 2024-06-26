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
        User registeredUser = userService.registerUser(user.getNumUser(), user.getType(), user.getSaldo());
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

    @PostMapping("/credit")
    public ResponseEntity<User> creditUser(@RequestBody User user){
        User registeredUser = userService.creditUser(user.getNumUser(), user.getSaldo());
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/transfer/{num_user_origin}/{num_user_destiny}")
    public ResponseEntity<User> transfer(@PathVariable String num_user_origin, @PathVariable String num_user_destiny, @RequestBody Double value) {
        User origin_user = userService.transfer(num_user_origin, num_user_destiny, value);
        return ResponseEntity.ok(origin_user);
    }

    @PostMapping("/yieldInterest/{interestRate}")
    public ResponseEntity<String> yieldInterest(@RequestBody Double interestRate){
        userService.yieldInterest(interestRate);
        return ResponseEntity.ok("successfully.");
    }
}
