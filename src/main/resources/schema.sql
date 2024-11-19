DROP TABLE Customer;
CREATE TABLE Customer (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    email_address VARCHAR(120) UNIQUE NOT NULL,
    phone_number VARCHAR(30) NOT NULL
);