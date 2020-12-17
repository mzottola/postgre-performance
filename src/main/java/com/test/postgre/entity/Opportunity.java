package com.test.postgre.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "opportunity_t")
@Data
public class Opportunity {

    @Column(name = "code")
    @Id
    private String code;

    public Opportunity() {

    }

    public Opportunity(String code) {
        this.code = code;
    }

    @JoinTable(
            name = "opportunity_user_t",
            joinColumns = @JoinColumn(name = "code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "username", referencedColumnName = "username")
    )
    @OneToMany
    private List<User> users;

}
