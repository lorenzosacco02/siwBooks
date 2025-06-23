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
INSERT INTO author (name, surname, date_of_birth, date_of_death, nationality) VALUES
                                                                                  ('Italo', 'Calvino', '1923-10-15', '1985-09-19', 'Italia'),
                                                                                  ('Giovanni', 'Verga', '1840-09-02', '1922-01-27', 'Italia'),
                                                                                  ('Natalia', 'Ginzburg', '1916-07-14', '1991-10-07', 'Italia'),
                                                                                  ('Cesare', 'Pavese', '1908-09-09', '1950-08-27', 'Italia'),
                                                                                  ('Gabriele', 'D''Annunzio', '1863-03-12', '1938-03-01', 'Italia'),
                                                                                  ('Luigi', 'Pirandello', '1867-06-28', '1936-12-10', 'Italia'),
                                                                                  ('Elena', 'Ferrante', '1943-08-01', null, 'Italia'),
                                                                                  ('Dacia', 'Maraini', '1936-11-13', null, 'Italia'),
                                                                                  ('J.R.R.', 'Tolkien', '1892-01-03', '1973-09-02', 'Regno Unito'),
                                                                                  ('George', 'Orwell', '1903-06-25', '1950-01-21', 'Regno Unito'),
                                                                                  ('Umberto', 'Eco', '1932-01-05', '2016-02-19', 'Italia'),
                                                                                  ('Antoine', 'de Saint-Exupéry', '1900-06-29', '1944-07-31', 'Francia'),
                                                                                  ('George R.R.', 'Martin', '1948-09-20', NULL, 'Stati Uniti'),
                                                                                  ('Jane', 'Austen', '1775-12-16', '1817-07-18', 'Regno Unito'),
                                                                                  ('J.K.', 'Rowling', '1965-07-31', NULL, 'Regno Unito'),
                                                                                  ('Giuseppe', 'Tomasi di Lampedusa', '1896-12-23', '1957-07-23', 'Italia'),
                                                                                  ('Italo', 'Svevo', '1861-12-19', '1928-09-13', 'Italia'),
                                                                                  ('Alessandro', 'Manzoni', '1785-03-07', '1873-05-22', 'Italia');


INSERT INTO book_authors (written_id, authors_id) VALUES (1, 9),
                                                         (2, 10),
                                                         (3, 11),
                                                         (4, 12),
                                                         (5, 13),
                                                         (6, 14),
                                                         (7, 15),
                                                         (8, 16),
                                                         (9, 17),
                                                         (10, 18);

-- ADMIN con password 'lollo'--
INSERT INTO users (name, surname, email, role) VALUES
    ('Lorenzo', 'Sacco', 'lorenzo.sacco@gmail.com', 'ADMIN');
INSERT INTO credentials (username, password, role, user_id) VALUES
    ('lollo', '$2a$10$Zvb18mH/1S.S.ljBRXJVwefA/qa.ebejmVPEIhOVlQk7QwTt2n2ou', 'ADMIN', 1);
