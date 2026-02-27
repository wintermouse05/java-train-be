-- ============================================
-- Database Schema for Student Management
-- ============================================

CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- User table (for authentication)
CREATE TABLE IF NOT EXISTS `user` (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Student table
CREATE TABLE IF NOT EXISTS student (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(20) NOT NULL,
    student_code VARCHAR(10) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Student Info table (1:1 with student)
CREATE TABLE IF NOT EXISTS student_info (
    info_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    address VARCHAR(255) NOT NULL,
    average_score DOUBLE,
    date_of_birth DATE,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
