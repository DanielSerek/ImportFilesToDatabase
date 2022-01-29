package com.example.importfilestodatabase.services;

import com.example.importfilestodatabase.models.Company;
import com.example.importfilestodatabase.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

@Service
public class CompanyServiceImpl implements CompanyService{

    CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    @Override
    public void saveCompany(String ico, String compName, String address, String email, String firstName, String surName) {
        Company company = new Company(ico, compName, address, email, firstName, surName);
        this.companyRepository.save(company);
    }

    @Override
    public void readFile() {
        Path currentRelativePath= Paths.get("");
        String currentPath=currentRelativePath.toAbsolutePath().toString();
        System.out.println("The current absolute path is: " + currentPath);

        // Read folder from the user
        // TBD

        // Read all files in the folder
        File folder = new File("C:\\Users\\Dan\\Documents\\CODING\\ImportFilesToDatabase\\import");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            System.out.println("-------------------------------------------------------------------------");
            if (file.isFile()) {
                System.out.println(file.getName());
                try{
                    Scanner scanner = new Scanner(file);
                    while(scanner.hasNext()){
                        System.out.println(scanner.nextLine());
                        String[] input = scanner.nextLine().split(",");
                        Company company = new Company();
                        company.setico(input[0]);
                        company.setcompName(input[1]);
                        company.setAddress(input[2] + ", " + input[3]);
                        company.setEmail(input[4]);
                        company.setFirtName(input[5]);
                        company.setSurName(input[6]);
                        company.setTimeStamp(new Date());
                        this.companyRepository.save(company);
                    }
                    scanner.close();
                }
                catch(Exception ex){
                }
            }
        }
    }
}
