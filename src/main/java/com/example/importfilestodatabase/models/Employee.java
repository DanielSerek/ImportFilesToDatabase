package com.example.importfilestodatabase.models;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;

@Entity
@Table(name="employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String surName;

    @OneToOne(mappedBy="employee", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Company company;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;

    public Employee(){
    }

    public Employee(String email, String firstName, String surName) {
        this.email = email;
        this.firstName = firstName;
        this.surName = surName;
        this.timeStamp = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
        this.timeStamp = new Date();
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int compareTo(Employee e) {
        return Comparator.comparing(Employee::getFirstName)
                .thenComparing(Employee::getSurName)
                .thenComparing(Employee::getEmail)
                .compare(this, e);
    }
}
