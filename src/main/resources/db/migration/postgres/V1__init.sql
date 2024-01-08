CREATE TABLE file_references(
     id   BIGSERIAL  NOT NULL ,
     uuid  VARCHAR(36) NOT NULL,
     file_type    VARCHAR(20) NOT NULL,
     file_name    VARCHAR(255),
     created_date TIMESTAMP NOT NULL,
     updated_date TIMESTAMP NOT NULL,
     PRIMARY KEY (id)
);

CREATE TABLE users (
    id BIGSERIAL NOT NULL ,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    email_confirmed BOOLEAN,
    phone VARCHAR(50),
    phone_displayed BOOLEAN,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    sex VARCHAR(6) NOT NULL,
    locale VARCHAR(2),
    avatar_id BIGINT,
    last_active_date TIMESTAMP,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
--     CONSTRAINT UK_user_email UNIQUE (email)
);

