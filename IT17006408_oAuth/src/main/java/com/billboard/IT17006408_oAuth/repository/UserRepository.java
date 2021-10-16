package com.billboard.IT17006408_oAuth.repository;


import com.billboard.IT17006408_oAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
