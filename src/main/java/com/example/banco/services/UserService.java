package com.example.banco.services;

import com.example.banco.entities.User;
import com.example.banco.exception.InsufficientFundsException;
import com.example.banco.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(String numUser){
        User user = new User();
        user.setNumUser(numUser);
        user.setSaldo(0);
        return userRepository.save(user);
    }

    public User getUserByNumUser(String num_user) {
        return userRepository.findByNumUser(num_user);
    }

    public User debitUser(String num_user, Double value) throws InsufficientFundsException {
        var user = userRepository.findByNumUser(num_user);
        if(user.getSaldo() < value){
            throw new InsufficientFundsException();
        }
        user.setSaldo(user.getSaldo()-value);
        return userRepository.save(user);
    }

    public User creditUser(String num_user, Double value) {
        var user = userRepository.findByNumUser(num_user);
        user.setSaldo(user.getSaldo()+value);
        return userRepository.save(user);
    }

    public User transfer(String num_user_origin, String num_user_destiny, Double value) throws InsufficientFundsException {
        var user_origin = debitUser(num_user_origin, value);
        var user_destiny = creditUser(num_user_destiny, value);

        return user_origin;
    }
}
