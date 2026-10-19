DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS applications;

CREATE TABLE applications (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    author  VARCHAR(255),
    name    VARCHAR(255),
    version VARCHAR(255)
);

CREATE TABLE users (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    email     VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    full_name VARCHAR(255)
);
