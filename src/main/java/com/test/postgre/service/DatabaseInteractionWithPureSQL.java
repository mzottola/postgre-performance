package com.test.postgre.service;

import static com.test.postgre.service.Util.createListOfUsers;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DatabaseInteractionWithPureSQL {

    private final EntityManager entityManager;

    @Autowired
    public DatabaseInteractionWithPureSQL(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void generateAndPersistEntities(final int numberOfEntities) {
        final String allValues = IntStream.rangeClosed(1, numberOfEntities)
                .mapToObj(it -> new EntityWithUserRightsRow("code_" + it, createListOfUsers(it % 2 == 0)))
                .map(it -> "('" + it.getCode() + "', " + it.getUsersAsString() + ")")
                .collect(Collectors.joining(","));
        entityManager.createNativeQuery("INSERT INTO t_entity_with_user_rights(code, users) VALUES" +allValues).executeUpdate();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<String> selectEntitiesUsingEntityManagerFor(String username) {
        return entityManager.createNativeQuery("SELECT code FROM t_entity_with_user_rights WHERE '" + username + "' = ANY(users)")
                .getResultList();
    }

    static class EntityWithUserRightsRow {
        private final String code;
        private final List<String> users;

        public EntityWithUserRightsRow(final String code, final List<String> users) {
            this.code = code;
            this.users = users;
        }

        public String getCode() {
            return code;
        }

        public String getUsersAsString() {
            return users.stream().collect(Collectors.joining(",", "'{", "}'"));
        }
    }
}
