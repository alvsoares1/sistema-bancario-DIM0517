package com.example.banco.services;

import com.example.banco.entities.User;
import com.example.banco.exception.InsufficientFundsException;
import com.example.banco.exception.NegativeValueException;
import com.example.banco.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(String numUser, int type) {
        User user = new User();
        user.setNumUser(numUser);
        user.setSaldo(0);
        if(type == 2){
            user.setPontos(10);
        }
        user.setType(type);
      
        return userRepository.save(user);
    }

    public User getUserByNumUser(String num_user) {
        return userRepository.findByNumUser(num_user);
    }

    public User debitUser(String num_user, Double value) {
        var user = userRepository.findByNumUser(num_user);
        if (user.getSaldo() < value) {
            throw new InsufficientFundsException();
        }
        user.setSaldo(user.getSaldo() - value);
        if (user.getType() == 2) {
            int pontos = (int) (value / 100);
            user.setPontos(user.getPontos() + pontos);
        }
        return userRepository.save(user);
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
        if(user_destiny.getType() == 2){
            int pontos_remover = (int) (value / 100);
            int pontos = (int) (value / 150);
            user_destiny.setPontos(user_destiny.getPontos()  -pontos_remover + pontos);
        }

        return user_origin;
    }

    public void yieldInterest(Double interestRate){
        List<User> userList = userRepository.findAll();
        for(User user : userList){
            if(user.getType() == 3){
                double yield = (interestRate/100)* user.getSaldo();
                user = creditUser(user.getId(), yield);
            }
        }
    }

    private void checkNegativeValue(Double value) {
        if(value < 0){
            throw new NegativeValueException();
        }
    }
}
