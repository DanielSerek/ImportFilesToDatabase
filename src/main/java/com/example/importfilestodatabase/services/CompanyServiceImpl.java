package com.example.importfilestodatabase.services;

import com.example.importfilestodatabase.models.Company;
import com.example.importfilestodatabase.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CompanyServiceImpl implements CompanyService{

    CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    @Override
    public void saveCompany(String ICO, String name, String address) {
        Company company = new Company(ICO, name, address);
        this.companyRepository.save(company);
    }
}
