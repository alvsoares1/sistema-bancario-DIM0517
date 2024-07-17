package com.example.banco.controllers;

import com.example.banco.entities.AtoTransferencia;
import com.example.banco.entities.User;
import com.example.banco.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conta")
public class UserController {
  @Autowired private UserService userService;

  /**
   * cadastra conta
   *
   * @param user - usuario para criação
   * @return ResponseEntity<User>
   */
  @PostMapping("/")
  public ResponseEntity<User> registerUser(@RequestBody User user) {
    User registeredUser =
        userService.registerUser(user.getNumUser(), user.getType(), user.getSaldo());
    return ResponseEntity.ok(registeredUser);
  }

  /**
   * consultar contas
   *
   * @return ResponseEntity<Iterable<User>>
   */
  @GetMapping("/")
  public ResponseEntity<Iterable<User>> getUsers() {
    var user = userService.getUsers();
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user);
  }

  /**
   * consultar conta
   *
   * @param numUser - id do usuário
   * @return ResponseEntity<User>
   */
  @GetMapping("/{numUser}")
  public ResponseEntity<User> getUserByNumUser(@PathVariable String numUser) {
    User user = userService.getUserByNumUser(numUser);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user);
  }

  /**
   * consultar saldo da conta
   *
   * @param numUser - id do usuário
   * @return ResponseEntity<Double>
   */
  @GetMapping("/{numUser}/saldo")
  public ResponseEntity<Double> getUserSaldoByNumUser(@PathVariable String numUser) {
    var value = userService.getSaldoByNumUser(numUser);
    if (value == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(value);
  }

  /**
   * creditar saldo da conta
   *
   * @param numUser - id do usuário
   * @return ResponseEntity<User>
   */
  @PutMapping("/{numUser}/credito")
  public ResponseEntity<User> creditUser(@PathVariable String numUser, @RequestBody User user) {
    User registeredUser = userService.creditUser(numUser, user.getSaldo());
    return ResponseEntity.ok(registeredUser);
  }

  /**
   * debitar saldo da conta
   *
   * @param numUser - id do usuário
   * @return ResponseEntity<User>
   */
  @PutMapping("{numUser}/debito")
  public ResponseEntity<User> debitUser(@PathVariable String numUser, @RequestBody User user) {
    var debitedUser = userService.debitUser(numUser, user.getSaldo());
    return ResponseEntity.ok(debitedUser);
  }

  /**
   * transferir entre contas
   *
   * @return ResponseEntity<User>
   */
  @PutMapping("/transferencia")
  public ResponseEntity<User> transfer(@RequestBody AtoTransferencia atoTransferencia) {
    User origin_user =
        userService.transfer(
            atoTransferencia.getFrom(), atoTransferencia.getTo(), atoTransferencia.getAmount());
    return ResponseEntity.ok(origin_user);
  }

  /**
   * render
   *
   * @return ResponseEntity<String>
   */
  @PutMapping("/rendimento")
  public ResponseEntity<String> yieldInterest(@RequestBody Double interestRate) {
    userService.yieldInterest(interestRate);
    return ResponseEntity.ok("successfully.");
  }
}
