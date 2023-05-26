INSERT INTO client (first_name, last_name, email) VALUES
('Jan', 'Kowalski', 'jan.kowalski@example.com'),
('Anna', 'Nowak', 'anna.nowak@example.com');

INSERT INTO book (title, author, blocked) VALUES
('Pan Tadeusz', 'Adam Mickiewicz', false),
('Lalka', 'Bolesław Prus', false);

INSERT INTO rental (client_id, book_id, start_date, end_date, returned) VALUES
(1, 1, '2023-05-01', '2023-05-31', false),
(2, 2, '2023-05-15', '2023-06-15', false);
