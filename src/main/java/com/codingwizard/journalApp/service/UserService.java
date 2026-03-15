package com.codingwizard.journalApp.service;

import com.codingwizard.journalApp.entity.Role;
import com.codingwizard.journalApp.entity.User;
import com.codingwizard.journalApp.repository.RoleRepository;
import com.codingwizard.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean saveNewUser(User user) {

        try {
        // encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // assign user role
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.getRoles().add(userRole);
        userRepository.save(user);
        return true;
    } catch (Exception e){
            log.error("Error occured for {} : " , user.getUserName(), e);
            return false;
        }
    }

    public void saveAdminUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role adminRole = roleRepository.findByName("ADMIN_USER");
        user.getRoles().add(adminRole);
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}