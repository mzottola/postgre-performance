package com.test.postgre.service;

import static com.test.postgre.service.Util.createListOfUsers;

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
public class DatabaseInteractionWithHibernate {

    private final EntityWithUserRightsRepository entityWithUserRightsRepository;
    private final EntityManager entityManager;

    @Autowired
    public DatabaseInteractionWithHibernate(final EntityWithUserRightsRepository entityWithUserRightsRepository,
                                            final EntityManager entityManager) {
        this.entityWithUserRightsRepository = entityWithUserRightsRepository;
        this.entityManager = entityManager;
    }

    @Transactional(rollbackFor = Exception.class)
    public void generateAndPersistEntities(int numberOfEntities) {
        final List<EntityWithUserRights> entitiesWithUserRights = IntStream.rangeClosed(1, numberOfEntities)
                .mapToObj(i -> new EntityWithUserRights("code_" + i, createListOfUsers(i % 2 == 0)))
                .collect(Collectors.toList());

        entityWithUserRightsRepository.saveAll(entitiesWithUserRights);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<EntityWithUserRights> selectEntitiesUsingJPAMappingFor(String username) {
        return entityWithUserRightsRepository.findAllByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll() {
        entityManager
                .createNativeQuery("DELETE FROM t_entity_with_user_rights where 1=1")
                .executeUpdate();
    }
}
