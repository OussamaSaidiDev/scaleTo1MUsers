INSERT INTO applications (author, name, version) VALUES
    ('James Gosling', 'Java Duke', '1.0.0'),
    ('Linus Torvalds', 'Git Tracker', '2.3.1'),
    ('Ada Lovelace', 'Analytical Engine', '0.9.0'),
    ('Grace Hopper', 'Cobol Compiler', '3.1.4'),
    ('Alan Turing', 'Enigma Breaker', '1.2.0'),
    ('Margaret Hamilton', 'Apollo Guidance', '4.0.2'),
    ('Dennis Ritchie', 'C Toolkit', '2.0.0'),
    ('Guido van Rossum', 'Python Notes', '3.12.0'),
    ('Barbara Liskov', 'Substitution Kit', '1.1.0'),
    ('Tim Berners-Lee', 'Web Browser', '5.0.1');

-- All sample users have the password: password123 (BCrypt-encoded)
INSERT INTO users (email, password, full_name) VALUES
    ('oussama@exemple.com', '$2a$10$PymNP5au3zYZJk1nt/L5XuFqSn8q3EZ1w74JL7eeV1kkFFdCht09S', 'Oussama Saidi'),
    ('john.doe@exemple.com', '$2a$10$PymNP5au3zYZJk1nt/L5XuFqSn8q3EZ1w74JL7eeV1kkFFdCht09S', 'John Doe'),
    ('jane.smith@exemple.com', '$2a$10$PymNP5au3zYZJk1nt/L5XuFqSn8q3EZ1w74JL7eeV1kkFFdCht09S', 'Jane Smith');
