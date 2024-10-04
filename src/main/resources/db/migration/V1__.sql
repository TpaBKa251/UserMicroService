CREATE TABLE banned_token
(
    token VARCHAR(255) NOT NULL,
    CONSTRAINT pk_banned_token PRIMARY KEY (token)
);

CREATE TABLE session
(
    id              UUID         NOT NULL,
    user_id         UUID         NOT NULL,
    token           VARCHAR(255) NOT NULL,
    create_time     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    expiration_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    active          BOOLEAN      NOT NULL,
    CONSTRAINT pk_session PRIMARY KEY (id)
);

CREATE TABLE "user"
(
    id                 UUID         NOT NULL,
    first_name         VARCHAR(255) NOT NULL,
    last_name          VARCHAR(255) NOT NULL,
    middle_name        VARCHAR(255),
    email              VARCHAR(255) NOT NULL,
    phone              VARCHAR(255),
    password           VARCHAR(255) NOT NULL,
    room_number        VARCHAR(255),
    group_number       VARCHAR(255),
    register_date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    update_date_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE session
    ADD CONSTRAINT uc_session_token UNIQUE (token);