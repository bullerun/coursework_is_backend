CREATE TYPE Role AS ENUM (
    'ROLE_USER',
    'ROLE_ADMIN'
    );

CREATE TABLE IF NOT EXISTS Users
(
    id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(31)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    salt     VARCHAR(255) NOT NULL,
    role     Role

);