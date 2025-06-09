-- LIBRI --
INSERT INTO book (title, publication_year) VALUES
                                                             ('Il Signore degli Anelli', 1954),
                                                             ('1984', 1949),
                                                             ('Il nome della rosa', 1980),
                                                             ('Il piccolo principe', 1943),
                                                             ('Il Trono Di Spade', 1996),
                                                             ('Orgoglio e pregiudizio', 1813),
                                                             ('Harry Potter e la pietra filosofale', 1997),
                                                             ('Il Gattopardo', 1958),
                                                             ('La coscienza di Zeno', 1923),
                                                             ('I Promessi Sposi', 1827);


-- AUTORI --
INSERT INTO author (name, surname, date_of_birth, date_of_death) VALUES ('Italo', 'Calvino', '1923-10-15', '1985-09-19'),
                                                                       ('Umberto', 'Eco', '1932-01-05', '2016-02-19'),
                                                                       ('Giovanni', 'Verga', '1840-09-02', '1922-01-27'),
                                                                       ('Natalia', 'Ginzburg', '1916-07-14', '1991-10-07'),
                                                                       ('Cesare', 'Pavese', '1908-09-09', '1950-08-27'),
                                                                       ('Gabriele', 'D''Annunzio', '1863-03-12', '1938-03-01'),
                                                                       ('Alessandro', 'Manzoni', '1785-03-07', '1873-05-22'),
                                                                       ('Luigi', 'Pirandello', '1867-06-28', '1936-12-10');
INSERT INTO author (name, surname, date_of_birth) VALUES ('Elena', 'Ferrante', '1943-08-01'),
                                                           ('Dacia', 'Maraini', '1936-11-13');


-- ADMIN con password 'lollo'--
INSERT INTO users (name, surname, username, password, email, role) VALUES
    ('Lorenzo', 'Sacco', 'lollo', '$2a$10$Zvb18mH/1S.S.ljBRXJVwefA/qa.ebejmVPEIhOVlQk7QwTt2n2ou', 'lorenzo.sacco@gmail.com', 'ADMIN');
INSERT INTO credentials (username, password, role, user_id) VALUES
    ('lollo', '$2a$10$Zvb18mH/1S.S.ljBRXJVwefA/qa.ebejmVPEIhOVlQk7QwTt2n2ou', 'ADMIN', 1);
