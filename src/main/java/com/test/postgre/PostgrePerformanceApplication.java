package com.test.postgre;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import com.test.postgre.service.DatabaseInteractionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@Slf4j
public class PostgrePerformanceApplication {

    private final DatabaseInteractionService databaseInteractionService;

    @Autowired
    public PostgrePerformanceApplication(final DatabaseInteractionService opportunityService) {
        this.databaseInteractionService = opportunityService;
    }

    public static void main(String[] args) {
        SpringApplication.run(PostgrePerformanceApplication.class, args);
    }

    @EventListener
    public void onReady(ApplicationReadyEvent event) {
        initializeDatabase();
        selectUsing2DifferentWays();
        deleteEveryRow();
    }

    private void initializeDatabase() {
        int numberOfEntities = 100_000;
        final long l = System.nanoTime();
        log.info("loading {} entities...", numberOfEntities);
        databaseInteractionService.generateAndPersistEntities(numberOfEntities);
        final long l2 = System.nanoTime();
        final long timeSpent = (l2 - l) / 1000000;
        log.info("{} entities inserted in {} ms", numberOfEntities, timeSpent);
    }

    private void selectUsing2DifferentWays() {
        final int userId = new Random().nextInt(120) + 1;
        final String username = "username_" + userId;
        selectUsingStandardMapping(username);
        selectUsingEntityManager(username);
    }

    private void selectUsingStandardMapping(String username) {
        final long l = System.nanoTime();
        log.info("select in database entities linked to {} using JPA repository...", username);
        final var entityWithUserRights = databaseInteractionService.selectEntitiesUsingJPAMappingFor(username);
        final long l2 = System.nanoTime();
        final long timeSpent = (l2 - l) / 1000000;
        log.info("{} entities retrieved in {} ms", entityWithUserRights.size(), timeSpent);
    }

    private void selectUsingEntityManager(String username) {
        final long l = System.nanoTime();
        log.info("select in database entities linked to {} using Entity Manager...", username);
        final var entityWithUserRights = databaseInteractionService.selectEntitiesUsingEntityManagerFor(username);
        final long l2 = System.nanoTime();
        final long timeSpent = (l2 - l) / 1000000;
        log.info("{} entities retrieved in {} ms", entityWithUserRights.size(), timeSpent);
    }

    private void deleteEveryRow() {
        log.info("Delete all...");
        databaseInteractionService.deleteAll();
        log.info("End");
    }
}
