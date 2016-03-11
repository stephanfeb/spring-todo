package com.teamoldspice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Person implements Serializable{
    @Id
    @Column(name="PERSON_ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = ".+@.+")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Boolean enabled = true;

    public Person(){}

    public Person(String username, String password){
        this.username = username;
        this.password = password;
        this.enabled = true;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    public Set<Authority> roles = new HashSet<>();

    @OneToMany()
    @JoinColumn(name="PERSON_ID")
    @OrderBy("ID ASC")
    public Set<Todo> todos = new HashSet<>();
}
