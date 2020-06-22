package com.reviewtrungtam.webapp.center.entity;

import javax.persistence.*;

@Entity
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
}
