package com.example.importfilestodatabase.services;

import com.example.importfilestodatabase.models.Company;
import com.example.importfilestodatabase.models.Employee;

import java.io.IOException;
import java.util.List;

public interface CompanyService {

    public void saveCompany(String ico, String compName, String address);

    void readDir() throws IOException;

    void readFiles() throws IOException;

    void showStatistics();

    public List<Company> getAllCompanies();

    public List<Employee> getAllEmployees();
}
