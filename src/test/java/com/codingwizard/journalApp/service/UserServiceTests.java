package com.codingwizard.journalApp.service;

import com.codingwizard.journalApp.entity.User;
import com.codingwizard.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public  void testfindByUserName(){
        assertNotNull(userRepository.findByUserName("Shobhan"));
    }

    @Test
    public void testJournalEntries(){
        User user = userRepository.findByUserName("Shobhan");
        assertTrue(!user.getJournalEntryList().isEmpty());
    }


    @Test
    public void test(){

    }
}
