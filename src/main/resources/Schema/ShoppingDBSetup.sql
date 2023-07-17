CREATE DATABASE IF NOT EXISTS user_db;
USE user_db;

CREATE TABLE IF NOT EXISTS User (
    userId 	        BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstName       VARCHAR(255),
    lastName        VARCHAR(255),
    email		    VARCHAR(255),
    password        VARCHAR(255),
    active          BOOLEAN,
    dateJoined      DATETIME,
    type            INT,
    status          INT
);

-- add sample users to table
INSERT INTO User (firstName, lastName, email, password, active, dateJoined, type, status) VALUES
('Admin', 'Test1', 'admin@email.com', '123', true, '2020-01-01', 0, 1),
('User', 'Test2', 'user@email.com', '123', true, '2020-01-02', 1, 1);