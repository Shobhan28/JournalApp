package com.codingwizard.journalApp.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @NonNull
    private String userName;
    @NonNull
    private String password;

    //  We need to establish parent child relation.
    //cascade means: Automatically save child when parent is saved.
    // Save user → Save journal entries
    //Delete user → Delete journal entries
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<JournalEntry> journalEntryList = new ArrayList<>();

}


