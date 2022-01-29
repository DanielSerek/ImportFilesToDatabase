package com.example.importfilestodatabase.services;

public interface CompanyService {

    void saveCompany(String ico, String compName, String address, String email, String firstName, String surName);

    void readFile();
}
