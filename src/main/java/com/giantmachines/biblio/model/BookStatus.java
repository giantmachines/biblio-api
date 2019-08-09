package com.giantmachines.biblio.model;

import javax.persistence.*;

@Entity
public class BookStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.ORDINAL)
    private Status value = Status.AVAILABLE;
}

enum Status { UNAVAILABLE, AVAILABLE, MISSING }
