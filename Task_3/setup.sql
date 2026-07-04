
-- setup.sql
-- MySQL Database Setup Script for Task 3
-- File-Based Persistent Student Management System → JDBC
--
-- Author      : Anuska Roy
-- Project     : JDBC-Based Student Management System – Task 3
-- Organisation: Maincrafts Technology
--
-- HOW TO RUN:
--   Option 1 – MySQL Workbench: Open file → Execute (Ctrl+Shift+Enter)
--   Option 2 – Command line:
--              mysql -u root -p < setup.sql
--   Option 3 – MySQL prompt:
--              mysql> SOURCE /path/to/setup.sql;




CREATE DATABASE IF NOT EXISTS student_db;

USE student_db;



DROP TABLE IF EXISTS students;



CREATE TABLE students (
    id   INT          NOT NULL,
    name VARCHAR(100) NOT NULL,
    age  INT          NOT NULL,
    CONSTRAINT pk_student PRIMARY KEY (id),
    CONSTRAINT chk_age    CHECK (age > 0 AND age <= 150)
);



INSERT INTO students (id, name, age) VALUES
    (201, 'Anuska Roy',      21),
    (202, 'Rahul Sharma',    22),
    (203, 'Priya Mehta',     20),
    (204, 'Siddharth Nair',  23),
    (205, 'Sneha Das',       21),
    (206, 'Arjun Verma',     24),
    (207, 'Kavya Pillai',    20);



SELECT * FROM students ORDER BY id;



