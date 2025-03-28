-- CODING CHALLENGE MYSQL
-- TASKS:
-- 1.Provide a SQL script that initializes the database for the Job Board scenario “CareerHub”.
CREATE DATABASE IF NOT EXISTS CareerHub;
USE CareerHub;
DROP DATABASE CareerHub;
-- 2. Create tables for Companies, Jobs, Applicants and Applications. 
-- Create Companies Table
CREATE TABLE IF NOT EXISTS Companies(
    companyID INT PRIMARY KEY AUTO_INCREMENT,
    companyName VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS Jobs(
    jobID INT PRIMARY KEY AUTO_INCREMENT,
    jobTitle VARCHAR(255) NOT NULL,
    jobDescription TEXT NOT NULL,
    jobLocation VARCHAR(255) NOT NULL,
    salary DECIMAL(10,2) CHECK (Salary >= 0),
    jobType ENUM('Full-time', 'Part-time', 'Contract', 'Internship') DEFAULT 'Full-time',
    postedDate DATETIME DEFAULT CURRENT_TIMESTAMP 
);


CREATE TABLE IF NOT EXISTS JobPostings(
    jobID INT PRIMARY KEY,
    companyID INT NOT NULL,
    FOREIGN KEY (JobID) REFERENCES Jobs(JobID) ON DELETE CASCADE,
    FOREIGN KEY (CompanyID) REFERENCES Companies(CompanyID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Applicants (
    applicantID INT PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    resume TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Applications (
    applicationID INT PRIMARY KEY AUTO_INCREMENT,
    applicantID INT NOT NULL,
    applicationDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    coverLetter TEXT NOT NULL,
    FOREIGN KEY (ApplicantID) REFERENCES Applicants(ApplicantID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS JobApplications (
    applicationID INT,
    jobID INT NOT NULL,
    PRIMARY KEY (ApplicationID, JobID),
    FOREIGN KEY (ApplicationID) REFERENCES Applications(ApplicationID) ON DELETE CASCADE,
    FOREIGN KEY (JobID) REFERENCES Jobs(JobID) ON DELETE CASCADE
);

-- 3. Define appropriate primary keys, foreign keys, and constraints.
-- 4. Ensure the script handles potential errors, such as if the database or tables already exist.

CREATE INDEX idx_job_location ON Jobs(jobLocation);
CREATE INDEX idx_applicant_email ON Applicants(email);
CREATE INDEX idx_application_date ON Applications(applicationDate);

/*
table relationship:
one Company can post many Jobs -> 1-to-Many (Companies → Jobs)
One Job can have multiple Applications -> 1-to-Many (Jobs → Applications)
One Applicant can apply to multiple Jobs -> 1-to-Many (Applicants → Applications)
JobPostings (Links Companies & Jobs)
JobApplications (Links Jobs & Applications)
*/

INSERT INTO Companies (companyName,location) VALUES
('HexaTech','New York'),
('InnovateX','San Francisco'),
('CodeSphere','Seattle'),
('ByteWorks','Chicago'),
('NexaSoft','Los Angeles'),
('CyberNest','Austin'),
('Quantum Systems','Boston'),
('DevPeak','Denver'),
('TechPioneers','Miami'),
('AI Edge','Dallas');

INSERT INTO Jobs (jobTitle, jobDescription, jobLocation, salary, jobType, postedDate) VALUES
('Software Engineer', 'Develop and maintain software applications.', 'New York', 90000.00, 'Full-time', '2025-03-10 10:30:00'),
('Data Analyst', 'Analyze large datasets to extract insights.', 'San Francisco', 75000.00, 'Full-time', '2025-03-11 12:45:00'),
('Frontend Developer', 'Build UI components with React.js.', 'Seattle', 85000.00, 'Full-time', '2025-03-12 09:00:00'),
('Backend Developer', 'Develop REST APIs using Java Spring Boot.', 'Chicago', 95000.00, 'Full-time', '2025-03-13 14:15:00'),
('Cloud Engineer', 'Manage cloud infrastructure on AWS.', 'Los Angeles', 105000.00, 'Full-time', '2025-03-14 11:10:00'),
('Cybersecurity Analyst', 'Monitor and secure IT systems.', 'Austin', 98000.00, 'Full-time', '2025-03-15 15:20:00'),
('AI Researcher', 'Develop machine learning models.', 'Boston', 120000.00, 'Full-time', '2025-03-16 13:30:00'),
('QA Engineer', 'Ensure software quality through testing.', 'Denver', 78000.00, 'Full-time', '2025-03-17 16:45:00'),
('DevOps Engineer', 'Automate CI/CD pipelines.', 'Miami', 110000.00, 'Full-time', '2025-03-18 10:05:00'),
('Project Manager', 'Manage software development projects.', 'Dallas', 115000.00, 'Full-time', '2025-03-19 09:50:00');

INSERT INTO JobPostings (jobID, companyID) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5),
(6, 6), (7, 7), (8, 8), (9, 9), (10, 10);

INSERT INTO Applicants (firstName, lastName, email, phone, resume) VALUES
('John', 'Doe', 'john.doe@email.com', '1234567890', 'Experienced Software Engineer'),
('Jane', 'Smith', 'jane.smith@email.com', '9876543210', 'Data Analyst with Python and SQL skills'),
('Robert', 'Brown', 'robert.brown@email.com', '5551236789', 'Frontend Developer skilled in React.js'),
('Emily', 'Davis', 'emily.davis@email.com', '4449871234', 'Backend Developer with expertise in Java'),
('Michael', 'Johnson', 'michael.johnson@email.com', '6667894321', 'Cloud Engineer certified in AWS'),
('Sarah', 'Miller', 'sarah.miller@email.com', '7775551234', 'Cybersecurity Analyst with ethical hacking skills'),
('David', 'Wilson', 'david.wilson@email.com', '8886669999', 'AI Researcher working on deep learning'),
('Emma', 'Taylor', 'emma.taylor@email.com', '9994447777', 'QA Engineer with test automation experience'),
('Daniel', 'Anderson', 'daniel.anderson@email.com', '1112223333', 'DevOps Engineer with Kubernetes expertise'),
('Sophia', 'Martinez', 'sophia.martinez@email.com', '3335556666', 'Project Manager with Agile experience');

INSERT INTO Applications (applicantID, applicationDate, coverLetter) VALUES
(1, '2025-03-20 10:30:00', 'Passionate about software engineering and problem-solving.'),
(2, '2025-03-21 11:15:00', 'Excited to work with data-driven solutions.'),
(3, '2025-03-22 09:45:00', 'Eager to contribute as a frontend developer.'),
(4, '2025-03-23 14:00:00', 'Strong backend development background.'),
(5, '2025-03-24 13:20:00', 'Looking forward to cloud-based projects.'),
(6, '2025-03-25 16:10:00', 'Passionate about cybersecurity.'),
(7, '2025-03-26 12:35:00', 'Deep learning enthusiast eager to contribute.'),
(8, '2025-03-27 15:50:00', 'Experienced in software testing.'),
(9, '2025-03-28 10:05:00', 'DevOps automation expert.'),
(10, '2025-03-29 09:30:00', 'Experienced in managing software projects.');

INSERT INTO JobApplications (ApplicationID, JobID) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5),
(6, 6), (7, 7), (8, 8), (9, 9), (10, 10);

INSERT INTO JobApplications (ApplicationID, JobID) VALUES
(1,3),(1,4),(5,8),(5,10);

show tables;
-- 5. Write an SQL query to count the number of applications received for each job listing in the 
-- "Jobs" table. Display the job title and the corresponding application count. Ensure that it lists all 
-- jobs, even if they have no applications.

SELECT * FROM Applications;
SELECT * FROM Jobs;
SELECT * FROM JobApplications;

INSERT INTO Jobs (jobTitle, jobDescription, jobLocation, salary, jobType, postedDate) VALUES
('Software Tester', 'Test software applications.', 'New York', 50000.00, 'Full-time', '2025-03-11 10:40:00');

SELECT 
	j.jobID,
    j.jobTitle,
    IFNULL(COUNT(ja.applicationID),0)as numberOfApplications
FROM Jobs j LEFT JOIN JobApplications ja USING (jobID)
GROUP BY j.jobID;

-- 6. Develop an SQL query that retrieves job listings from the "Jobs" table within a specified salary 
-- range. Allow parameters for the minimum and maximum salary values. Display the job title, 
-- company name, location, and salary for each matching job.
SET @minSal=60000;
SET @maxSal=90000;

select * from JobPostings;

SELECT 
	j.jobID,
    j.jobTitle,
    c.companyName,
    j.jobLocation,
    j.salary
FROM Jobs j
	JOIN JobPostings jp ON j.jobID=jp.jobID
    JOIN companies c ON c.companyID=jp.companyID
WHERE salary BETWEEN @minSal AND @maxSal;


-- 7. Write an SQL query that retrieves the job application history for a specific applicant. Allow a 
-- parameter for the ApplicantID, and return a result set with the job titles, company names, and 
-- application dates for all the jobs the applicant has applied to.
SET @applicantID = 1; 

SELECT 
	ja.jobID,
    j.jobTitle,
    c.companyName,
    a.applicationDate
FROM Applications a
JOIN JobApplications ja ON a.applicationID = ja.applicationID
JOIN Jobs j ON ja.jobID = j.jobID
JOIN JobPostings jp ON j.jobID = jp.jobID
JOIN Companies c ON jp.companyID = c.companyID
WHERE a.applicantID = @applicantID;

-- 8. Create an SQL query that calculates and displays the average salary offered by all companies for 
-- job listings in the "Jobs" table. Ensure that the query filters out jobs with a salary of zero.

SELECT AVG(salary) AS averageSalary FROM jobs WHERE salary > 0;

SELECT 
    c.companyID, 
    c.companyName, 
    AVG(j.salary) AS averageSalary
FROM Companies c
JOIN JobPostings jp ON c.companyID = jp.companyID
JOIN Jobs j ON jp.jobID = j.jobID
WHERE j.salary > 0
GROUP BY c.companyID, c.companyName;

-- 9. Write an SQL query to identify the company that has posted the most job listings. Display the 
-- company name along with the count of job listings they have posted. Handle ties if multiple 
-- companies have the same maximum count.

SELECT 
	c.companyName, 
    COUNT(jp.jobID) AS jobCount
FROM companies c
	JOIN jobpostings jp USING (companyID)
GROUP BY c.companyID
HAVING jobCount = (
	SELECT MAX(jobCount) 
		FROM (
			SELECT COUNT(jobID) AS jobCount FROM jobpostings 
			GROUP BY companyID) AS job_counts
            );


-- 10. Find the applicants who have applied for positions in companies located in 'CityX' and have at 
-- least 3 years of experience.
SET @city='New York';
SELECT 
    a.applicantID, 
    a.firstName, 
    a.lastName, 
    a.yrsExperience, 
    c.companyID, 
    c.companyName, 
    c.location
FROM Applicants a
JOIN Companies c
WHERE c.location = @city
AND a.yrsExperience >= 3;


-- 11. Retrieve a list of distinct job titles with salaries between $60,000 and $80,000.

SELECT DISTINCT jobTitle FROM jobs 
WHERE salary BETWEEN 60000 AND 80000;

-- 12. Find the jobs that have not received any applications.

SELECT * FROM jobs;
select * from jobApplications;
INSERT INTO `careerhub`.`jobs` (`jobID`, `jobTitle`, `jobDescription`, `jobLocation`, `salary`, `jobType`, `postedDate`) 
VALUES ('12', 'Scrum master', 'Manage Sprints', 'Chennai', '90000.00', 'Full-time', '2025-03-08 10:40:00');

SELECT 
    j.jobID, 
    j.jobTitle, 
    j.jobDescription, 
    j.salary
FROM jobs j
LEFT JOIN jobapplications ja ON j.jobID = ja.jobID
WHERE ja.applicationID IS NULL;

-- 13. Retrieve a list of job applicants along with the companies they have applied to and the positions 
-- they have applied for.
SELECT 
    a.applicantID,
    CONCAT(a.firstName, ' ', a.lastName) AS applicantName,
    c.companyID,
    c.companyName,
    j.jobID,
    j.jobTitle
FROM applications app
JOIN applicants a ON app.applicantID = a.applicantID
JOIN jobapplications ja ON app.applicationID = ja.applicationID
JOIN jobs j ON ja.jobID = j.jobID
JOIN jobpostings jp ON j.jobID = jp.jobID
JOIN companies c ON jp.companyID = c.companyID;

-- 14. Retrieve a list of companies along with the count of jobs they have posted, even if they have not 
-- received any applications.
SELECT * FROM JOBPOSTINGS;

SELECT 
    c.companyID,
    c.companyName,
    COUNT(jp.jobID) AS totalJobsPosted
FROM companies c
LEFT JOIN jobpostings jp ON c.companyID = jp.companyID
GROUP BY c.companyID, c.companyName;

-- 15. List all applicants along with the companies and positions they have applied for, including those 
-- who have not applied.
SELECT * FROM companies;
SELECT * FROM applicants;

SELECT 
    a.applicantID,
    CONCAT(a.firstName, ' ', a.lastName) AS applicantName,
    c.companyID,
    c.companyName,
    j.jobID,
    j.jobTitle
FROM applicants a
LEFT JOIN applications ap ON a.applicantID = ap.applicantID
LEFT JOIN jobapplications ja ON ap.applicationID = ja.applicationID
LEFT JOIN jobs j ON ja.jobID = j.jobID
LEFT JOIN jobpostings jp ON j.jobID = jp.jobID
LEFT JOIN companies c ON jp.companyID = c.companyID;

-- 16. Find companies that have posted jobs with a salary higher than the average salary of all jobs.
SELECT 
    c.companyID,
    c.companyName,
    j.jobTitle,
    j.salary
FROM Companies c 
JOIN JobPostings jp USING (companyID)
LEFT JOIN Jobs j USING (jobID)
WHERE j.salary >(
    SELECT AVG(salary) FROM Jobs
);
SELECT AVG(salary) FROM Jobs;
-- 17. Display a list of applicants with their names and a concatenated string of their city and state.
alter table applicants add column city varchar(50) not null;
alter table applicants add column state varchar(50) not null;

UPDATE `careerhub`.`applicants` SET `city` = 'Nungambaakam', `state` = 'Chennai' WHERE (`applicantID` = '1');
UPDATE `careerhub`.`applicants` SET `city` = 'Thoripaakkam', `state` = 'Chennai' WHERE (`applicantID` = '2');
UPDATE `careerhub`.`applicants` SET `city` = 'Anna Street', `state` = 'Namakkal' WHERE (`applicantID` = '3');
UPDATE `careerhub`.`applicants` SET `city` = 'Nungambaakam', `state` = 'Chennai' WHERE (`applicantID` = '4');
UPDATE `careerhub`.`applicants` SET `city` = 'Nungambaakam', `state` = 'Chennai' WHERE (`applicantID` = '7');
UPDATE `careerhub`.`applicants` SET `city` = 'Nungambaakam', `state` = 'Chennai' WHERE (`applicantID` = '10');
UPDATE `careerhub`.`applicants` SET `city` = 'pune', `state` = 'hyderabad' WHERE (`applicantID` = '5');
UPDATE `careerhub`.`applicants` SET `city` = 'pune', `state` = 'hyderabad' WHERE (`applicantID` = '6');
UPDATE `careerhub`.`applicants` SET `city` = 'ranches', `state` = 'bangalore' WHERE (`applicantID` = '8');
UPDATE `careerhub`.`applicants` SET `city` = 'ranches', `state` = 'bangalore' WHERE (`applicantID` = '9');
UPDATE `careerhub`.`applicants` SET `city` = 'bikhol', `state` = 'calicut' WHERE (`applicantID` = '11');

SELECT
    CONCAT(firstName, ' ', lastName) AS applicantName,
    CONCAT(city, ', ', state) AS Address
FROM Applicants;

-- 18. Retrieve a list of jobs with titles containing either 'Developer' or 'Engineer'.
SELECT 
    jobID,
    jobTitle,
    jobDescription
FROM Jobs 
WHERE jobTitle LIKE '%Developer%' OR jobTitle LIKE '%Engineer%';

-- 19. Retrieve a list of applicants and the jobs they have applied for, including those who have not 
-- applied and jobs without applicants.
INSERT INTO `careerhub`.`applicants` (`applicantID`, `firstName`, `lastName`, `email`, `phone`, `resume`, `yrsExperience`) 
VALUES ('11', 'Riya', 'Jose', 'riya.jose@email.com', '9876453275', 'Software Developer with Java', '0');

SELECT 
    a.applicantID,
    CONCAT(a.firstName, ' ', a.lastName) AS applicantName,
    IFNULL(j.jobTitle, 'Not Applied') AS jobTitle
FROM Applicants a
LEFT JOIN Applications ap USING (applicantID)
LEFT JOIN JobApplications ja USING (applicationID)
LEFT JOIN Jobs j USING (jobID);

-- 20. List all combinations of applicants and companies where the company is in a specific city and the 
-- applicant has more than 2 years of experience. For example: city=Chennai
select * from applicants;
select * from companies;

ALTER TABLE applicants ADD COLUMN yrsExperience INT DEFAULT 0;
UPDATE `careerhub`.`applicants` SET `yrsExperience` = '1' WHERE (`applicantID` = '3');
UPDATE `careerhub`.`applicants` SET `yrsExperience` = '4' WHERE (`applicantID` = '4');
UPDATE `careerhub`.`applicants` SET `yrsExperience` = '7' WHERE (`applicantID` = '5');
UPDATE `careerhub`.`applicants` SET `yrsExperience` = '3' WHERE (`applicantID` = '2');
UPDATE `careerhub`.`applicants` SET `yrsExperience` = '2' WHERE (`applicantID` = '1');
UPDATE `careerhub`.`applicants` SET `yrsExperience` = '2' WHERE (`applicantID` = '8');
UPDATE `careerhub`.`applicants` SET `yrsExperience` = '2' WHERE (`applicantID` = '10');
UPDATE `careerhub`.`applicants` SET `yrsExperience` = '2' WHERE (`applicantID` = '6');

UPDATE `careerhub`.`companies` SET `location` = 'New York' WHERE (`companyID` = '10');
UPDATE `careerhub`.`companies` SET `location` = 'New York' WHERE (`companyID` = '5');

SET @city='New York';
SELECT 
    a.applicantID, 
    a.firstName, 
    a.lastName, 
    a.yrsExperience, 
    c.companyID, 
    c.companyName, 
    c.location
FROM Applicants a
CROSS JOIN Companies c
WHERE c.location = @city
AND a.yrsExperience > 2;