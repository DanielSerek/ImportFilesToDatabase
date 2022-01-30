package com.example.importfilestodatabase.repositories;

import com.example.importfilestodatabase.models.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
