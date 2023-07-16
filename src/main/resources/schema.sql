CREATE TABLE IF NOT EXISTS clients (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS books (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255),
  author VARCHAR(255),
  available BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS rentals (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  client_id BIGINT,
  book_id BIGINT,
  start_date DATE,
  end_date DATE,
  returned BOOLEAN NOT NULL,
   version INTEGER NOT NULL,
  FOREIGN KEY (client_id) REFERENCES clients(id),
  FOREIGN KEY (book_id) REFERENCES books(id)
);
