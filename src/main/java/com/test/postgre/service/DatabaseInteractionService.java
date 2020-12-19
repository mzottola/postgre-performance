package com.test.postgre.service;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.test.postgre.entity.EntityWithUserRights;
import com.test.postgre.repository.EntityWithUserRightsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DatabaseInteractionService {

    private final EntityWithUserRightsRepository entityWithUserRightsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DatabaseInteractionService(final EntityWithUserRightsRepository entityWithUserRightsRepository) {
        this.entityWithUserRightsRepository = entityWithUserRightsRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void generateAndPersistEntities(int numberOfOpportunities) {
        final List<EntityWithUserRights> opportunities = IntStream.rangeClosed(1, numberOfOpportunities)
                .mapToObj(i -> new EntityWithUserRights("code_" + i, createListOfUsers(i % 2 == 0)))
                .collect(Collectors.toList());

        entityWithUserRightsRepository.saveAll(opportunities);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<EntityWithUserRights> selectEntitiesUsingJPAMappingFor(String username) {
        return entityWithUserRightsRepository.findAllByUsername(username);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<String> selectEntitiesUsingEntityManagerFor(String username) {
        final String sqlUsername = "'" + username + "'";
        return entityManager.createNativeQuery("SELECT code FROM t_entity_with_user_rights WHERE " + sqlUsername + " = ANY(users)")
                .getResultList();
    }

    private List<String> createListOfUsers(boolean even) {
        int numberOfUsers = even ? 100 : 120;
        return IntStream.range(1, numberOfUsers)
                .mapToObj(it -> "username_" + it)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll() {
        entityManager
                .createNativeQuery("DELETE FROM t_entity_with_user_rights where 1=1")
                .executeUpdate();
    }
}
