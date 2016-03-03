package com.teamoldspice.model;

import javax.persistence.*;

@Entity
public class Authority {

    @Id
    @Column(name="AUTHORITY_ID")
    private Long id;
    private String authority;

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

//    @ManyToOne
//    @JoinColumn(name="PERSON_ID")
//    private Person owner;
}
