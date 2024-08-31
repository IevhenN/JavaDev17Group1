CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL CHECK (char_length(username) >= 5),
    password VARCHAR(100) NOT NULL CHECK (char_length(password) >= 8),
    enabled BOOLEAN NOT NULL DEFAULT true
);


CREATE TABLE note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL
    //для тестудодала
    //user_id BIGINT REFERENCES users(id)
);