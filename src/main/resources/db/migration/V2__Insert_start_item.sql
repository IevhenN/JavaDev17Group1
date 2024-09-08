INSERT INTO users (username,password)
VALUES ('admin','$2a$10$9mdH0hw1vbukRPHX8GmuoOCWQPwHpHiiDiHin5eJej/0NoXbeH3oq'),
       ('user2', '$2a$10$ebB9xagf3I6FGy51dKpfieOQH5WDDSy.lgd.RXCJUzXiAcOi0xfU2');

INSERT INTO note (id, user_id, title, content, access_type, date)
VALUES (UUID(), 1, 'Start note', 'Start content', 'PRIVATE', '07-09-2024 14:53');
