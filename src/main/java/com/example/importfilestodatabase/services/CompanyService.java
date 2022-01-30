package com.example.importfilestodatabase.services;

import java.io.IOException;

public interface CompanyService {

    void saveCompany(String ico, String compName, String address);

    void readDir() throws IOException;

    void readFiles() throws IOException;

    void showStatistics();

}
