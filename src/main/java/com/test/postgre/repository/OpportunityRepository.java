package com.test.postgre.repository;

import java.util.List;

import com.test.postgre.entity.Opportunity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OpportunityRepository extends JpaRepository<Opportunity, String> {

    @Query("FROM Opportunity o INNER JOIN FETCH o.users u")
    List<Opportunity> findAllByJoinTable();
}
