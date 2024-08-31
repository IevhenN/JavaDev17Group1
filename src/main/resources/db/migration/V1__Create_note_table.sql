CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL CHECK (char_length(username) >= 5),
    password VARCHAR(100) NOT NULL CHECK (char_length(password) >= 8),
    enabled BOOLEAN NOT NULL DEFAULT true
);


CREATE TABLE IF NOT EXISTS note (
    id VARCHAR(36) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL CHECK (LENGTH(title) BETWEEN 5 AND 100),
    content TEXT NOT NULL CHECK (LENGTH(content) BETWEEN 5 AND 10000),
    access_type VARCHAR(10) NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);

