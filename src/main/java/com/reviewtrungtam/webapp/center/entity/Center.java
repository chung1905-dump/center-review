package com.reviewtrungtam.webapp.center.entity;

import javax.persistence.*;

@Entity
public class Center {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private int point;
}
