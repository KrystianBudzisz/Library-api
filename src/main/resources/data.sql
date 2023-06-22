INSERT INTO clients (first_Name, last_Name) VALUES ('John', 'Doe');
INSERT INTO clients (first_Name, last_Name) VALUES ('Jane', 'Doe');

INSERT INTO books (title, author, available) VALUES ('Book One', 'Author One', true);
INSERT INTO books (title, author, available) VALUES ('Book Two', 'Author Two', true);

INSERT INTO rentals (client_id, book_id, start_date, end_date, returned) VALUES (1, 1, '2023-01-01', '2023-01-31', false);
INSERT INTO rentals (client_id, book_id, start_date, end_date, returned) VALUES (2, 2, '2023-01-01', '2023-01-31', false);
