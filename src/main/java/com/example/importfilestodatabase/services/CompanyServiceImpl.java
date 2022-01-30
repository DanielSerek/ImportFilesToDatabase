package com.example.importfilestodatabase.services;

import com.example.importfilestodatabase.models.Company;
import com.example.importfilestodatabase.models.Employee;
import com.example.importfilestodatabase.models.Status;
import com.example.importfilestodatabase.repositories.CompanyRepository;
import com.example.importfilestodatabase.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Value("${custom.properties.path}")
    private String customPath;
    private CompanyRepository companyRepository;
    private EmployeeRepository employeeRepository;
    private static final Logger LOG = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private ArrayList<File> listOfFiles = new ArrayList<>();
    private HashMap<Status, Integer> stats = new HashMap<Status, Integer>();

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveCompany(String ico, String compName, String address) {
        Company company = new Company(ico, compName, address);
        this.companyRepository.save(company);
    }

    @Override
    public void readDir() throws IOException{

        LOG.info("----------------------------------------------------------------------------------------------------------------------------------");

        // Initialize values for the individual statuses in the statistics HashMap
        for (Status status : Status.values()) {
            stats.put(status, 0);
        }

        // Read all files in the folder
        File folder = new File(customPath);
        File[] readFiles = folder.listFiles();

        // Add only .csv files into the list of files
        for (File file : readFiles) {
            if (file.getName().endsWith(".csv")) {
                listOfFiles.add(file);
                stats.put(Status.FOUND, stats.get(Status.FOUND) + 1);
            }
        }
        LOG.info("All files have been checked.");
    }

    @Override
    public void readFiles() throws IOException {
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    LOG.info("Reading file: " + file.getName());
                    Scanner scanner = new Scanner(file, "UTF-8");
                    while (scanner.hasNext()) {
                        String[] input = scanner.nextLine().split(",");
                        Company company = new Company();
                        Employee employee = new Employee();
                        try {
                            company.setico(input[0]);
                            company.setcompName(input[1]);
                            company.setAddress(input[2] + ", " + input[3]);
                            company.setTimeStamp(new Date());
                            employee.setEmail(input[4]);
                            employee.setFirstName(input[5]);
                            employee.setSurName(input[6]);
                            employee.setTimeStamp(new Date());
                            compareAndSaveData(company, employee);
                        } catch (IndexOutOfBoundsException e) {
                            LOG.info("The record is not in a required format.");
                            stats.put(Status.ERROR, stats.get(Status.ERROR) + 1);
                        }
                    }
                    LOG.info("The file " + file.getName() + " has been processed.");
                    scanner.close();
                } catch (Exception e) {
                    System.out.println("!!! Exception encountered: " + e.getMessage());
                }
                moveProcessedFile(file);
            }
        }
    }

    @Transactional
    public void compareAndSaveData(Company newCompany, Employee newEmployee) throws IOException {
        for (Company existingCompany : getAllCompanies()) {
            if(isICOsame(existingCompany, newCompany)){
                existingCompany.setAddress(newCompany.getAddress());
                existingCompany.setCompName(newCompany.getCompName());
                existingCompany.setEmployee(newEmployee);
                existingCompany.setTimeStamp(new Date());
                try{
                    this.companyRepository.save(existingCompany);
                    stats.put(Status.UPDATED, stats.get(Status.UPDATED) + 1);
                    return;
                }
                catch (DataIntegrityViolationException e) {
                    LOG.info("A duplicate record was identified. ");
                    stats.put(Status.DUPLICATE, stats.get(Status.DUPLICATE) + 1);
                }
                catch(Exception e){
                    LOG.info("Error. It wasn't possible to save the record in the database.");
                    stats.put(Status.ERROR, stats.get(Status.ERROR) + 1);
                }
            }
        }
        try{
            this.employeeRepository.save(newEmployee);
            newCompany.setEmployee(newEmployee);
            this.companyRepository.save(newCompany);
            stats.put(Status.PROCESSED, stats.get(Status.PROCESSED) + 1);
            return;
        }
        catch (DataIntegrityViolationException e) {
            LOG.info("A duplicate record was identified. ");
            stats.put(Status.DUPLICATE, stats.get(Status.DUPLICATE) + 1);
        }
        catch(Exception e){
            LOG.info("Error. It wasn't possible to save the record in the database.");
            stats.put(Status.ERROR, stats.get(Status.ERROR) + 1);
        }
    }

    private boolean isICOsame(Company firstCompany, Company secondCompany){
        String input1 = firstCompany.getIco().replaceAll("[\uFEFF-\uFFFF]", "");
        String input2 = secondCompany.getIco().replaceAll("[\uFEFF-\uFFFF]", "");
        for (int i = 0; i < 8; i++) {
            if(input1.charAt(i)!=input2.charAt(i)) return false;
        }
        return true;
    }

    @Override
    public ArrayList<Company> getAllCompanies(){
        return (ArrayList<Company>) this.companyRepository.findAll();
    }

    @Override
    public ArrayList<Employee> getAllEmployees(){
        return (ArrayList<Employee>) this.employeeRepository.findAll();
    }

    private void moveProcessedFile(File file){
        try {
            String path = customPath + File.separator + ".." + File.separator + "/processed/";
            Path movedFile = Paths.get(path + file.getName());
            Path originalPath = file.toPath();
            Files.copy(originalPath, movedFile, StandardCopyOption.REPLACE_EXISTING);
            //file.delete();
            LOG.info("The file " + file.getName() + " was saved here.");
        }
        catch(Exception e){
            LOG.info("The file " + file.getName() + " couldn't have been saved.");
        }
    }

    @Override
    public void showStatistics() {
        LOG.info("The statistics of data transfer");
        LOG.info("The number of processed files: " + stats.get(Status.FOUND));
        LOG.info("The number of processed records: " + stats.get(Status.PROCESSED));
        LOG.info("The number of updated records: " + stats.get(Status.UPDATED));
        LOG.info("The number of duplicate records: " + stats.get(Status.DUPLICATE));
        LOG.info("The number of errors in the records: " + stats.get(Status.ERROR));
    }
}