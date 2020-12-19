drop table if exists t_entity_with_user_rights;
create table t_entity_with_user_rights
(
    code varchar,
    users text[]
);
