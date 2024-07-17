package com.example.banco;

import com.example.banco.controllers.UserController;
import com.example.banco.entities.User;
import com.example.banco.exception.InsufficientFundsException;
import com.example.banco.exception.MinFundsExceededException;
import com.example.banco.exception.NegativeValueException;
import com.example.banco.repositories.UserRepository;
import com.example.banco.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BancoApplicationTests {

  @Autowired private UserController userController;

  @Autowired private UserService userService;

  @Autowired private UserRepository repository;

  private User user;

  // write test cases here

  @BeforeEach
  void contextLoads() {
    repository.deleteAll();
    user = new User();
    user.setNumUser("1");
    user.setType(1);
    repository.save(user);
  }

  /*
  Deve-se criar pelo menos os seguintes testes:

  ▪ Consultar Conta – Obs. Diferentes testes para cada tipo de conta
         1 = Normal
         2 = Bonificada
         3 = Poupança
   */

  @Test
  public void cadastrarContaNormal() {
    var user2 = new User();
    user2.setNumUser("2");
    user2.setSaldo(50);

    var userAction =
        userService.registerUser(user2.getNumUser(), user2.getType(), user2.getSaldo());
    user2.setSaldo(0);
    assertMesmosDados(userAction, user2);
  }

  @Test
  public void cadastrarContaBonificada() {
    var user2 = new User();
    user2.setNumUser("2");
    user2.setType(2);
    user2.setSaldo(50);

    var userAction =
        userService.registerUser(user2.getNumUser(), user2.getType(), user2.getSaldo());
    user2.setSaldo(0);
    user2.setPontos(10);
    assertMesmosDados(userAction, user2);
  }

  @Test
  public void cadastrarContaPoupanca() {
    var user2 = new User();
    user2.setNumUser("2");
    user2.setType(3);
    user2.setSaldo(50);

    var userAction =
        userService.registerUser(user2.getNumUser(), user2.getType(), user2.getSaldo());
    assertMesmosDados(userAction, user2);
  }

  @Test
  public void consultarContaNormal() {
    var user2 = new User();
    user2.setNumUser("2");
    user2.setSaldo(50);

    var userAction =
        userService.registerUser(user2.getNumUser(), user2.getType(), user2.getSaldo());
    user2 = userService.getUserByNumUser(user2.getNumUser());
    assertMesmosDados(userAction, user2);
  }

  @Test
  public void consultarContaBonificada() {
    var user2 = new User();
    user2.setNumUser("2");
    user2.setType(2);
    user2.setSaldo(50);

    var userAction =
        userService.registerUser(user2.getNumUser(), user2.getType(), user2.getSaldo());
    user2 = userService.getUserByNumUser(user2.getNumUser());
    assertMesmosDados(userAction, user2);
  }

  @Test
  public void consultarContaPoupanca() {
    var user2 = new User();
    user2.setNumUser("2");
    user2.setType(3);
    user2.setSaldo(50);

    var userAction =
        userService.registerUser(user2.getNumUser(), user2.getType(), user2.getSaldo());
    user2 = userService.getUserByNumUser(user2.getNumUser());
    assertMesmosDados(userAction, user2);
  }

  @Test
  public void consultarSaldoConta() {
    user.setSaldo(50);
    repository.save(user);

    var userAction = userService.getSaldoByNumUser(user.getNumUser());
    Assertions.assertThat(userAction).isEqualTo(user.getSaldo());
  }

  @Test
  public void creditarValorPositivoService() {
    user.setSaldo(50);
    var userAction = userService.creditUser(user.getNumUser(), user.getSaldo());
    Assertions.assertThat(userAction.getSaldo()).isEqualTo(user.getSaldo());

    userAction = userService.creditUser(user.getNumUser(), user.getSaldo());
    Assertions.assertThat(userAction.getSaldo()).isEqualTo(user.getSaldo() + user.getSaldo());
  }

  @Test
  public void creditarValorNegativoService() {
    user.setSaldo(-50);
    Assertions.assertThatExceptionOfType(NegativeValueException.class)
        .isThrownBy(() -> userService.creditUser(user.getNumUser(), user.getSaldo()));
  }

  @Test
  void creditarValorContaBonusService() {
    user.setType(2);
    user.setSaldo(100);
    repository.save(user);
    var userBonus = userService.creditUser(user.getNumUser(), user.getSaldo());
    Assertions.assertThat(userBonus.getPontos())
        .isEqualTo(user.getPontos() + ((int) user.getSaldo() / 100));
  }

  @Test
  void debitarValorPositivo() {
    user.setSaldo(50);
    userController.debitUser(user.getNumUser(), user);
  }

  @Test
  void debitarValorPositivoService() {
    user.setSaldo(50);
    userService.debitUser(user.getNumUser(), user.getSaldo());
  }

  @Test
  void debitarValorNegativo() {
    user.setSaldo(-50);
    Assertions.assertThatExceptionOfType(NegativeValueException.class)
        .isThrownBy(() -> userController.debitUser(user.getNumUser(), user));
  }

  @Test
  void debitarValorNegativoService() {
    user.setSaldo(-50);
    Assertions.assertThatExceptionOfType(NegativeValueException.class)
        .isThrownBy(() -> userService.debitUser(user.getNumUser(), user.getSaldo()));
  }

  @Test
  void debitarValorMaior() {
    user.setSaldo(user.getSaldo() + 1);
    userController.debitUser(user.getNumUser(), user);
  }

  @Test
  void debitarValorMaiorService() {
    user.setSaldo(user.getSaldo() + 1);
    userService.debitUser(user.getNumUser(), user.getSaldo());
  }

  @Test
  void debitarValorMaiorChequeService() {
    user.setSaldo(user.getSaldo() + 1001);
    Assertions.assertThatExceptionOfType(MinFundsExceededException.class)
        .isThrownBy(() -> userService.debitUser(user.getNumUser(), user.getSaldo()));
  }

  @Test
  void debitarValorMaiorTipo2Service() {
    user.setType(3);
    repository.save(user);
    user.setSaldo(user.getSaldo() + 1);
    Assertions.assertThatExceptionOfType(InsufficientFundsException.class)
        .isThrownBy(() -> userService.debitUser(user.getNumUser(), user.getSaldo()));
  }

  @Test
  void transferirEntreContasService() {
    var user2 = new User();
    user2.setNumUser("2");
    int valor = 50;
    user2.setSaldo(valor);
    repository.save(user2);
    user2 = userService.transfer(user2.getNumUser(), user.getNumUser(), user2.getSaldo());
    user = repository.findByNumUser(user.getNumUser());
    Assertions.assertThat(user2.getSaldo()).isEqualTo(0);
    Assertions.assertThat(user.getSaldo()).isEqualTo(valor);
  }

  @Test
  void transferirEntreContasEspecialService() {
    var user2 = new User();
    user2.setNumUser("2");
    user2.setSaldo(50);
    repository.save(user2);
    userService.transfer(user2.getNumUser(), user.getNumUser(), user2.getSaldo() + 1);
  }

  @Test
  void transferirEntreContasNegativoService() {
    var user2 = new User();
    user2.setType(3);
    user2.setNumUser("2");
    user2.setSaldo(50);
    repository.save(user2);
    Assertions.assertThatExceptionOfType(InsufficientFundsException.class)
        .isThrownBy(
            () ->
                userService.transfer(
                    user2.getNumUser(), user.getNumUser(), user2.getSaldo() + 1001));
  }

  @Test
  void transferirEntreContasBonificacaoService() {
    int valor = 200;
    var user2 = new User();
    user2.setType(2);
    user2.setNumUser("2");
    user2.setSaldo(valor);
    repository.save(user2);
    user2 = userService.transfer(user2.getNumUser(), user.getNumUser(), user2.getSaldo());
    user = repository.findByNumUser(user.getNumUser());
    Assertions.assertThat(user2.getPontos()).isEqualTo((int) (valor / 100));
  }

  @Test
  void renderJuros() {
    double perc = 2;
    int valor = 100;
    user.setType(3);
    user.setNumUser("2");
    user.setSaldo(valor);
    repository.save(user);
    userService.yieldInterest(perc);
    user = repository.findByNumUser(user.getNumUser());
    Assertions.assertThat(user.getSaldo()).isEqualTo(valor + valor * perc / 100);
  }

  private void assertMesmosDados(User userAction, User user2) {
    Assertions.assertThat(userAction.getSaldo()).isEqualTo(user2.getSaldo());
    Assertions.assertThat(userAction.getType()).isEqualTo(user2.getType());
    Assertions.assertThat(userAction.getNumUser()).isEqualTo(user2.getNumUser());
  }
}
