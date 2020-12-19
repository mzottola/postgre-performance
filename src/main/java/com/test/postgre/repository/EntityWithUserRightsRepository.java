package com.test.postgre.repository;

import java.util.List;

import com.test.postgre.entity.EntityWithUserRights;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EntityWithUserRightsRepository extends JpaRepository<EntityWithUserRights, String> {

    @Query(value = "SELECT * FROM t_entity_with_user_rights WHERE :username = ANY(users)", nativeQuery = true)
    List<EntityWithUserRights> findAllByUsername(String username);
}
