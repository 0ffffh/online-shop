CREATE TABLE users (
id SERIAL PRIMARY KEY,
name VARCHAR(50) UNIQUE NOT NULL,
password VARCHAR(50) NOT NULL,
role VARCHAR(10) NOT NULL);
