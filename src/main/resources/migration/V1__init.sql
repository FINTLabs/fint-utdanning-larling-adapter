create table otungdom
(
    id                 varchar(255) not null,
    last_modified_date timestamp,
    org_id             varchar(255),
    resource           json,
    primary key (id)
);
