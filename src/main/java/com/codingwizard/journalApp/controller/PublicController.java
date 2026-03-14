package com.codingwizard.journalApp.controller;


import com.codingwizard.journalApp.entity.User;
import com.codingwizard.journalApp.service.UserDetailsServiceImpl;
import com.codingwizard.journalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/public")

public class PublicController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-admin")
    public String createAdmin(@RequestBody User user){
        userService.saveAdminUser(user);
        return "Admin Created";
    }
}
