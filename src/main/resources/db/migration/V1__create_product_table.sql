CREATE TABLE products (
id SERIAL PRIMARY KEY,
name VARCHAR(100),
price REAL NOT NULL,
creation_date TIMESTAMP);