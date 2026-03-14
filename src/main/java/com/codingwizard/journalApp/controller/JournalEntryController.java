package com.codingwizard.journalApp.controller;

import com.codingwizard.journalApp.entity.JournalEntry;
import com.codingwizard.journalApp.entity.User;
import com.codingwizard.journalApp.service.JournalEntryService;
import com.codingwizard.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        //First we fetch the user from database using username.
        User user = userService.findByUserName(userName);

        if (user == null){
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        // Now get the list of journal entries associated with that user
        List<JournalEntry> entries = user.getJournalEntryList();

        // if user has the entries we will return them
        if (entries != null && !entries.isEmpty()) {
            return new ResponseEntity<>(entries, HttpStatus.OK);
        }

        //if no entries exists
        return new ResponseEntity<>("No journal entries found", HttpStatus.NOT_FOUND);
    }

    //create a new journal entry for a user.
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try {
            // First we check if user exists
            User user = userService.findByUserName(userName);

            if (user == null) {
                return new ResponseEntity<>("User not fount", HttpStatus.NOT_FOUND);
            }
            //Now we save journal entry using service layer
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating entry" , HttpStatus.BAD_REQUEST);
        }
    }

    //get a journal entry by id
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable Long id) {

        // here we directly fetch journal entry using i=ID from database
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        return new ResponseEntity<>("Entry not found" , HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{userName}/id/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable Long id, @PathVariable String userName) {
        boolean removed = journalEntryService.deleteById(id, userName);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>("Entry not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable Long id, @RequestBody JournalEntry newEntry) {

            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if (journalEntry.isPresent()) {
                // fetch existing entry from database
                JournalEntry old = journalEntry.get();

                // update title only if new title is provided
                if(newEntry.getTitle() != null && !newEntry.getTitle().equals("")) {
                    old.setTitle(newEntry.getTitle());
                }

                // update contant only if new content is provided
                if (newEntry.getContent() != null && !newEntry.getContent().equals("")) {
                    old.setContent(newEntry.getContent());
                }

                //save the updated entry back to database
                journalEntryService.saveEntry(old);

                return new ResponseEntity<>(old, HttpStatus.OK);
            }
            return new ResponseEntity<>("Entry not found" ,HttpStatus.NOT_FOUND);
        }
}