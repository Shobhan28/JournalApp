package com.codingwizard.journalApp.Controller;

import com.codingwizard.journalApp.Entity.JournalEntry;
import com.codingwizard.journalApp.Repository.JournalEntryRepository;
import com.codingwizard.journalApp.Repository.UserRepository;
import com.codingwizard.journalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {


    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getJournal")
    public List<JournalEntry> getAllJournalEntriesOfUser(){
       return  journalEntryRepository.findAll();

    }

    @PostMapping("/postJournal")
    public ResponseEntity<JournalEntry>  createEntry(@RequestBody JournalEntry journalEntry) {
        try {
            journalEntryRepository.save(journalEntry);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournelById(@PathVariable Long myId){
        Optional<JournalEntry> journalEntry = journalEntryRepository.findById(myId);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable Long myId){
         journalEntryRepository.deleteById(myId);
    }

    @PutMapping("id/{id}")
    public  JournalEntry updateEntryById(@PathVariable Long id, @RequestBody JournalEntry myEntry){
        myEntry.setId(id);
        return journalEntryRepository.save(myEntry);
    }

}
