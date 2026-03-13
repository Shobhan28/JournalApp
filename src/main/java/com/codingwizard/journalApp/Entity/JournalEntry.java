package com.codingwizard.journalApp.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import jakarta.persistence.*;



@Entity
@Data
@NoArgsConstructor
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private  String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
