CREATE DATABASE IF NOT EXISTS CareerHubDb;
USE CareerHubDb;
drop database careerhubdb;


CREATE TABLE IF NOT EXISTS Users (
    userID INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    userType ENUM('APPLICANT', 'COMPANY') NOT NULL
);


CREATE TABLE IF NOT EXISTS Companies (
    companyID INT PRIMARY KEY AUTO_INCREMENT,
    companyName VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    FOREIGN KEY (companyID) REFERENCES Users(userID)
);

CREATE TABLE IF NOT EXISTS Applicants (
    applicantID INT PRIMARY KEY AUTO_INCREMENT,
    Resume VARCHAR(255),
    FOREIGN KEY (applicantID) REFERENCES Users(userID)
);


CREATE TABLE IF NOT EXISTS JobListings (
    jobID INT PRIMARY KEY AUTO_INCREMENT,
    companyID INT NOT NULL,
    jobTitle VARCHAR(100) NOT NULL,
    jobDescription TEXT,
    jobLocation VARCHAR(100),
    salary DECIMAL(10, 2) CHECK (Salary >= 0),
    jobType ENUM('Full-time', 'Part-time', 'Contract'),
    postedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    -- deadline
    FOREIGN KEY (companyID) REFERENCES Companies(companyID)
);
ALTER TABLE JobListings
ADD COLUMN deadline DATETIME;

CREATE TABLE IF NOT EXISTS JobApplications (
    applicationID INT PRIMARY KEY AUTO_INCREMENT,
    jobID INT NOT NULL,
    applicantID INT NOT NULL,
    applicationDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    coverLetter TEXT,
    FOREIGN KEY (jobID) REFERENCES JobListings(jobID) ON DELETE CASCADE,
    FOREIGN KEY (applicantID) REFERENCES Applicants(applicantID) ON DELETE CASCADE
);

select * from companies JOIN users on companyID=userId;
select * from JobListings;
select * from users;
select * from applicants JOIN users on applicantID=userId;
Select * from JobApplications;
truncate JobApplications;
