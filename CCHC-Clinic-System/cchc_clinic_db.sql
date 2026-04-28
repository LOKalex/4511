CREATE DATABASE IF NOT EXISTS cchc_clinic DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;
USE cchc_clinic;

-- User Table
CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('PATIENT', 'STAFF', 'ADMIN') NOT NULL,
    assigned_clinic_id INT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clinic Table
CREATE TABLE clinic (
    clinic_id INT AUTO_INCREMENT PRIMARY KEY,
    clinic_name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    opening_hours TEXT NOT NULL,
    walkin_enabled BOOLEAN DEFAULT TRUE
);

-- Service Table
CREATE TABLE service (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(100) NOT NULL,
    service_description TEXT,
    duration_minutes INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    requires_approval BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE
);

-- Appointment Table
CREATE TABLE appointment (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    clinic_id INT NOT NULL,
    service_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'ARRIVED', 'COMPLETED', 'NO_SHOW', 'CANCELLED') DEFAULT 'PENDING',
    approval_note TEXT,
    visit_outcome TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES user(user_id),
    FOREIGN KEY (clinic_id) REFERENCES clinic(clinic_id),
    FOREIGN KEY (service_id) REFERENCES service(service_id)
);

-- Walk-in Queue Table
CREATE TABLE walkin_queue (
    queue_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    clinic_id INT NOT NULL,
    service_id INT NOT NULL,
    queue_date DATE NOT NULL,
    queue_number INT NOT NULL,
    status ENUM('WAITING', 'CALLED', 'SKIPPED', 'COMPLETED', 'EXPIRED') DEFAULT 'WAITING',
    called_time TIME NULL,
    completed_time TIME NULL,
    estimated_wait_minutes INT DEFAULT 0,
    FOREIGN KEY (patient_id) REFERENCES user(user_id),
    FOREIGN KEY (clinic_id) REFERENCES clinic(clinic_id),
    FOREIGN KEY (service_id) REFERENCES service(service_id),
    UNIQUE KEY unique_queue (clinic_id, service_id, queue_date, queue_number)
);

-- Operational Issue Table
CREATE TABLE operational_issue (
    issue_id INT AUTO_INCREMENT PRIMARY KEY,
    clinic_id INT NOT NULL,
    reported_by_staff_id INT NOT NULL,
    issue_type VARCHAR(50) NOT NULL,
    issue_description TEXT NOT NULL,
    status ENUM('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') DEFAULT 'OPEN',
    reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at DATE NULL,
    resolution_note TEXT,
    FOREIGN KEY (clinic_id) REFERENCES clinic(clinic_id),
    FOREIGN KEY (reported_by_staff_id) REFERENCES user(user_id)
);

INSERT INTO clinic (clinic_name, address, phone, opening_hours, walkin_enabled) 
VALUES ('CCHC Central Clinic', '123 Main St, Central', '2123-4567', 'Mon-Fri: 9am-6pm', TRUE);

INSERT INTO user (username, password, full_name, role, assigned_clinic_id) 
VALUES ('staff1', 'password123', 'Dr. Test', 'STAFF', 1);