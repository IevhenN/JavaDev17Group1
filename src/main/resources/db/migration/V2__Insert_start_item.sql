INSERT INTO users (username,password)
VALUES ('user','$2a$10$feSgAmXnxsmeSysS9CmkEOXhLedNlE9ERZ3UgBlkhS4YA38fCncPO');

INSERT INTO note (id, user_id, title, content, access_type)
VALUES (UUID(), 1, 'Start note', 'Start content', 'PRIVATE');