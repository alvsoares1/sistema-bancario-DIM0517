package com.example.banco.services;

import com.example.banco.entities.User;
import com.example.banco.exception.InsufficientFundsException;
import com.example.banco.exception.MinFundsExceededException;
import com.example.banco.exception.NegativeValueException;
import com.example.banco.repositories.UserRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;

  public User registerUser(String numUser, int type, double saldoInicial) {

    User user = new User();
    user.setNumUser(numUser);
    if (type == 2) {
      user.setPontos(10);
    }
    if (type == 3) {
      user.setSaldo(saldoInicial);
    } else {
      user.setSaldo(0);
    }
    user.setType(type);

    return userRepository.save(user);
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public User getUserByNumUser(String num_user) {
    return userRepository.findByNumUser(num_user);
  }

  public Double getSaldoByNumUser(String num_user) {
    var user = userRepository.findByNumUser(num_user);
    if (user == null) {
      return null;
    }
    return user.getSaldo();
  }

  public User debitUser(String num_user, Double value) {
    checkNegativeValue(value);
    var user = userRepository.findByNumUser(num_user);
    validateFunds(value, user);
    user.setSaldo(user.getSaldo() - value);
    if (user.getType() == 2) {
      int pontos = (int) (value / 100);
      user.setPontos(user.getPontos() + pontos);
    }
    return userRepository.save(user);
  }

  private static void validateFunds(Double value, User user) {
    if (user != null && user.getType() < 3) {
      if (user.getSaldo() < value - 1000) throw new MinFundsExceededException();
    } else if (user.getSaldo() < value) {
      throw new InsufficientFundsException();
    }
  }

  public User creditUser(String num_user, Double value) {
    checkNegativeValue(value);
    var user = userRepository.findByNumUser(num_user);
    user.setSaldo(user.getSaldo() + value);

    if (user.getType() == 2) {
      int pontos = (int) (value / 100);
      user.setPontos(user.getPontos() + pontos);
    }

    return userRepository.save(user);
  }

  public User transfer(String num_user_origin, String num_user_destiny, Double value) {
    var user_origin = debitUser(num_user_origin, value);
    var user_destiny = creditUser(num_user_destiny, value);

    return user_origin;
  }

  public void yieldInterest(Double interestRate) {
    if (Objects.isNull(interestRate) || interestRate < 0) {
      interestRate = 1.01;
    }
    List<User> userList = userRepository.findAll();
    for (User user : userList) {
      if (user.getType() == 3) {
        double yield = (interestRate / 100) * user.getSaldo();
        user = creditUser(user.getNumUser(), yield);
      }
    }
  }

  private void checkNegativeValue(Double value) {
    if (value < 0) {
      throw new NegativeValueException();
    }
  }
}
