package com.codingwizard.journalApp.controller;

import com.codingwizard.journalApp.entity.User;
import com.codingwizard.journalApp.repository.UserRepository;
import com.codingwizard.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

 /*   @GetMapping
    public List<User> getAllUsers(){
        return userService.getAll();
    }*/

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User users){
        userService.saveNewUser(users);
        return ResponseEntity.ok("User Created");
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User users) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDB = userService.findByUserName(users.getUserName());
        if (userInDB != null) {
            userInDB.setUserName(users.getUserName());
            userInDB.setPassword(users.getPassword());
            userService.saveUser(userInDB);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
