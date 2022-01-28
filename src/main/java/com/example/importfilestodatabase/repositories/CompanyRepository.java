package com.example.importfilestodatabase.repositories;

import com.example.importfilestodatabase.models.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
    List<Company> findAll();
    Optional<Company> findByName(String ICO);
}
