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
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="employee_id", referencedColumnName = "id")
    private Employee employee;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;

    public Company() {
    }

    public Company(String ico, String compName, String address) {
        this.ico = ico;
        this.compName = compName;
        this.address = address;
        this.timeStamp = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
