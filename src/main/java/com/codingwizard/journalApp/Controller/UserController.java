package com.codingwizard.journalApp.Controller;

import com.codingwizard.journalApp.Entity.User;
import com.codingwizard.journalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping
    public void createUser(@RequestBody User users){
        userService.saveUser(users);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User users) {
        User userInDB = userService.findByUserName(users.getUserName());
        if (userInDB != null) {
            userInDB.setUserName(users.getUserName());
            userInDB.setPassword(users.getPassword());
            userService.saveUser(userInDB);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
