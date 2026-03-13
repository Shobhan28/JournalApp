package com.codingwizard.journalApp.Repository;

import com.codingwizard.journalApp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);
}
