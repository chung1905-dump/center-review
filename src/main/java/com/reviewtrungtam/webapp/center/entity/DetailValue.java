package com.reviewtrungtam.webapp.center.entity;

import javax.persistence.*;

@Entity
public class DetailValue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Center center;

    @ManyToOne
    private Detail detail;

    private String value;
}
