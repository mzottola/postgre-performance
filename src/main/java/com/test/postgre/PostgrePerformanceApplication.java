package com.test.postgre;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.Random;

import com.test.postgre.service.DatabaseInteractionWithHibernate;
import com.test.postgre.service.DatabaseInteractionWithPureSQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@Slf4j
public class PostgrePerformanceApplication {

    private final DatabaseInteractionWithHibernate databaseInteractionWithHibernate;
    private final DatabaseInteractionWithPureSQL databaseInteractionWithPureSQL;
    private final EntityManager entityManager;

    @Autowired
    public PostgrePerformanceApplication(final DatabaseInteractionWithHibernate databaseInteractionWithHibernate,
                                         final DatabaseInteractionWithPureSQL databaseInteractionWithPureSQL,
                                         final EntityManager entityManager) {
        this.databaseInteractionWithHibernate = databaseInteractionWithHibernate;
        this.databaseInteractionWithPureSQL = databaseInteractionWithPureSQL;
        this.entityManager = entityManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(PostgrePerformanceApplication.class, args);
    }

    @EventListener
    public void onReady(ApplicationReadyEvent event) {
        int numberOfEntities = 100_000;
        final int userId = new Random().nextInt(120) + 1;
        final String username = "username_" + userId;

        log.info("USE HIBERNATE");
        useHibernate(numberOfEntities, username);
        log.info("deleting every entity");

        databaseInteractionWithHibernate.deleteAll();

        log.info("USE PURE SQL");
        usePureSQL(numberOfEntities, username);
    }

    private void useHibernate(int numberOfEntities, String username) {
        initializeDatabaseWithHibernate(numberOfEntities);
        selectUsingStandardMapping(username);

    }

    private void initializeDatabaseWithHibernate(int numberOfEntities) {
        final long l = System.nanoTime();
        log.info("loading {} entities...", numberOfEntities);
        databaseInteractionWithHibernate.generateAndPersistEntities(numberOfEntities);
        final long l2 = System.nanoTime();
        final long timeSpent = (l2 - l) / 1000000;
        log.info("{} entities inserted in {} ms", numberOfEntities, timeSpent);
    }

    private void selectUsingStandardMapping(String username) {
        final long l = System.nanoTime();
        log.info("select in database entities linked to {} using JPA repository...", username);
        final var entityWithUserRights = databaseInteractionWithHibernate.selectEntitiesUsingJPAMappingFor(username);
        final long l2 = System.nanoTime();
        final long timeSpent = (l2 - l) / 1000000;
        log.info("{} entities retrieved in {} ms", entityWithUserRights.size(), timeSpent);
    }

    private void usePureSQL(int numberOfEntities, String username) {
        initializeDatabaseUsingPureSQL(numberOfEntities);
        selectUsingPureSQL(username);
    }

    private void initializeDatabaseUsingPureSQL(int numberOfEntities) {
        final long l = System.nanoTime();
        log.info("loading {} entities...", numberOfEntities);
        databaseInteractionWithPureSQL.generateAndPersistEntities(numberOfEntities);
        final long l2 = System.nanoTime();
        final long timeSpent = (l2 - l) / 1000000;
        log.info("{} entities inserted in {} ms", numberOfEntities, timeSpent);
    }

    private void selectUsingPureSQL(String username) {
        final long l = System.nanoTime();
        log.info("select in database entities linked to {} using Entity Manager...", username);
        final var entityWithUserRights = databaseInteractionWithPureSQL.selectEntitiesUsingEntityManagerFor(username);
        final long l2 = System.nanoTime();
        final long timeSpent = (l2 - l) / 1000000;
        log.info("{} entities retrieved in {} ms", entityWithUserRights.size(), timeSpent);
    }

}
