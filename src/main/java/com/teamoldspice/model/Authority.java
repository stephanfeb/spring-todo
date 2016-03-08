package com.teamoldspice.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Authority implements Serializable{

    @Id
    @Column(name="AUTHORITY_ID")
    private Long id;
    private String authority;

    public Authority(){}

    public Authority(String role){
        this.authority = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authority)) return false;

        Authority authority1 = (Authority) o;

        return getAuthority().equals(authority1.getAuthority());
    }


//    @ManyToOne
//    @JoinColumn(name="PERSON_ID")
//    public Person owner;
}
