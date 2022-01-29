package com.example.importfilestodatabase.services;

import com.example.importfilestodatabase.models.Company;
import com.example.importfilestodatabase.models.Status;
import com.example.importfilestodatabase.repositories.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
    CompanyRepository companyRepository;
    private static final Logger LOG = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private ArrayList<File> listOfFiles = new ArrayList<>();
    private HashMap<Status, Integer> stats = new HashMap<Status, Integer>();

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void saveCompany(String ico, String compName, String address, String email, String firstName, String surName) {
        Company company = new Company(ico, compName, address, email, firstName, surName);
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

        // Read individual files
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    LOG.info("Reading file: " + file.getName());
                    Scanner scanner = new Scanner(file, "UTF-8");
                    while (scanner.hasNext()) {
                        String[] input = scanner.nextLine().split(",");
                        Company company = new Company();
                        try {
                            company.setico(input[0]);
                            company.setcompName(input[1]);
                            company.setAddress(input[2] + ", " + input[3]);
                            company.setEmail(input[4]);
                            company.setFirtName(input[5]);
                            company.setSurName(input[6]);
                            company.setTimeStamp(new Date());
                        } catch (IndexOutOfBoundsException e) {
                            LOG.info("The record is not in a required format.");
                            stats.put(Status.ERROR, stats.get(Status.ERROR) + 1);
                        }
                        try {
                            this.companyRepository.save(company);
                            stats.put(Status.PROCESSED, stats.get(Status.PROCESSED) + 1);
                        } catch (DataIntegrityViolationException e) {
                            LOG.info("A duplicate record was identified. ");
                            stats.put(Status.DUPLICATE, stats.get(Status.DUPLICATE) + 1);
                        }
                    }
                    LOG.info("The file " + file.getName() + " has been processed.");
                    scanner.close();
                } catch (Exception e) {
                    System.out.println("!!! Exception encountered: " + e.getMessage());
                }

                // Save processed file
                try {
                    String path = customPath + File.separator + ".." + File.separator + "/processed/";
                    Path movedFile = Paths.get(path + file.getName());
                    Path originalPath = file.toPath();
                    Files.copy(originalPath, movedFile, StandardCopyOption.REPLACE_EXISTING);
                    LOG.info("The file " + file.getName() + " was saved here.");
                }
                catch(Exception e){
                    LOG.info("The file " + file.getName() + " couldn't have been saved.");
                }
            }
        }
    }

    @Override
    public void showStatistics() {
        LOG.info("The statistics of data transfer");
        LOG.info("The number of processed files: " + stats.get(Status.FOUND));
        LOG.info("The number of processed records: " + stats.get(Status.PROCESSED));
        LOG.info("The number of duplicate records: " + stats.get(Status.DUPLICATE));
        LOG.info("The number of errors in the records: " + stats.get(Status.ERROR));
    }
}