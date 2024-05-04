package com.example.banco.services;

import com.example.banco.entities.User;
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

    public User debitUser(String num_user, Double value) {
        var user = userRepository.findByNumUser(num_user);
        user.setSaldo(user.getSaldo()-value);
        return userRepository.save(user);
    }

    public User creditar(String num_user, Double value) {
        var user = userRepository.findByNumUser(num_user);
        user.setSaldo(user.getSaldo()+value);
        return userRepository.save(user);
    }
}
