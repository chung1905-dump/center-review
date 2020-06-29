package com.reviewtrungtam.webapp.review.entity;

import com.reviewtrungtam.webapp.user.entity.User;

import javax.persistence.*;

@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Review review;

    private int vote;
}
