package com.test.postgre.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

import com.vladmihalcea.hibernate.type.array.ListArrayType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Table(name = "t_entity_with_user_rights")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class EntityWithUserRights {

    @Id
    @Column(name = "code")
    private String code;

    // INFO this JPA mapping leads to bad performance at select, related to number of "EntityWithUserRights" to load
    @Type(type = "list-array")
    @Column(name = "users", columnDefinition = "text[]")
    private List<String> users;

}
