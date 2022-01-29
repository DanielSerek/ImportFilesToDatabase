package com.example.importfilestodatabase.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name="ICO")
    private String ico;
    private String compName;
    private String address;
    @Column(unique = true)
    private String email;
    private String firtName;
    private String surName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;

    public Company() {
    }

    public Company(String ico, String compName, String address, String email, String firstName, String surName) {
        this.ico = ico;
        this.compName = compName;
        this.address = address;
        this.email = email;
        this.firtName = firstName;
        this.surName = surName;
        this.timeStamp = new Date();
    }

    public String getico() {
        return ico;
    }

    public void setico(String ico) {
        this.ico = ico;
    }

    public String getName() {
        return compName;
    }

    public void setcompName(String compName) {
        this.compName = compName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirtName() {
        return firtName;
    }

    public void setFirtName(String firtName) {
        this.firtName = firtName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
