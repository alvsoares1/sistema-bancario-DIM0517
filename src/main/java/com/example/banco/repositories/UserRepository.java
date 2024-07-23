package com.example.banco.repositories;

import com.example.banco.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByNumUser(String num_user);
}
