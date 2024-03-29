package com.example.importfilestodatabase;

import com.example.importfilestodatabase.services.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImportFilesToDatabaseApplication
        implements CommandLineRunner {

    private final CompanyService companyService;

    public ImportFilesToDatabaseApplication(CompanyService companyService) {
        this.companyService = companyService;
    }

    private static Logger LOG = LoggerFactory
            .getLogger(ImportFilesToDatabaseApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(ImportFilesToDatabaseApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws Exception{
        LOG.info("EXECUTING : command line runner");
        companyService.readDir();
        companyService.readFiles();
        companyService.showStatistics();
        }
    }

