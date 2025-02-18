package com.github.henriquemb.nlw_connect.repository;

import com.github.henriquemb.nlw_connect.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
