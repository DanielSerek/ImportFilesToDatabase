package com.example.importfilestodatabase.models;

import javax.persistence.*;

@Entity
@Table(name="companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name="ICO")
    private String ico;
    private String name;
    private String address;

    public Company() {
    }

    public Company(String ico, String name, String address) {
        this.ico = ico;
        this.name = name;
        this.address = address;
    }

    public String getico() {
        return ico;
    }

    public void setico(String ico) {
        this.ico = ico;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
