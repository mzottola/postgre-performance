package com.test.postgre.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_t")
@Data
public class User {

    @Id
    @Column(name = "username")
    private String username;

    public User() {

    }

    public User(String username) {
        this.username = username;
    }

}
