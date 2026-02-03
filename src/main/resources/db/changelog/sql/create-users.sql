CREATE TABLE users (
                       id BIGINT PRIMARY KEY,
                       is_blocked BOOLEAN DEFAULT FALSE NOT NULL
);