package com.codingwizard.journalApp.Service;

import com.codingwizard.journalApp.Entity.JournalEntry;
import com.codingwizard.journalApp.Entity.User;
import com.codingwizard.journalApp.Repository.JournalEntryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;



    // Save journal entry for a specific user
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {

        try {

            // Step 1: Find the user from database using username
            User user = userService.findByUserName(userName);

            if(user == null){
                throw new RuntimeException("User not found");
            }

            // Step 2: Set the user inside journal entry
            // This is important because in MySQL we maintain
            // relationship using foreign key (user_id)
            journalEntry.setUser(user);

            // Step 3: Save journal entry into database
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry);

            // Step 4: Add this journal entry into user's list
            // so that both sides of the relationship remain synced
            user.getJournalEntryList().add(savedEntry);

            // Step 5: Save the user again to update relationship
            userService.saveUser(user);

        } catch (Exception e) {
            log.error("Error saving journal entry", e);
            throw new RuntimeException("An error occurred while saving the entry.", e);
        }
    }



    // This method is used when we just want to save entry
    // without linking it with username
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }



    // Fetch all journal entries from database
    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }



    // Find a specific journal entry using ID
    public Optional<JournalEntry> findById(Long id) {
        return journalEntryRepository.findById(id);
    }



    // Delete journal entry for a user
    @Transactional
    public boolean deleteById(Long id, String userName) {

        boolean removed = false;

        try {

            // Step 1: Fetch user
            User user = userService.findByUserName(userName);

            if(user == null){
                return false;
            }

            // Step 2: Remove journal entry from user's list
            // removeIf checks every entry and removes matching id
            removed = user.getJournalEntryList()
                    .removeIf(entry -> entry.getId().equals(id));

            if(removed){

                // Step 3: Save updated user
                userService.saveUser(user);

                // Step 4: Delete entry from database
                journalEntryRepository.deleteById(id);
            }

        } catch (Exception e) {
            log.error("Error deleting journal entry", e);
            throw new RuntimeException("An error occurred while deleting the entry.", e);
        }

        return removed;
    }
}