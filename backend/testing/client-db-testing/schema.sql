CREATE DATABASE client_db;

\c client_db;

CREATE TABLE IF NOT EXISTS clients (
  id SERIAL PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  ingredients TEXT,
  favorite_recipes INTEGER[],
  CONSTRAINT unique_username UNIQUE (username)
);


CREATE DATABASE recipe_db;

\c recipe_db;

CREATE TABLE IF NOT EXISTS recipes (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  ingredients TEXT,
  prep_time TIME NOT NULL,
  cook_time TIME NOT NULL,
  total_time TIME NOT NULL,
  image_url TEXT NOT NULL,
  instructions TEXT NOT NULL,
  notes TEXT
);

CREATE TABLE IF NOT EXISTS client_recipes (
  id SERIAL PRIMARY KEY,
  client_id INT,
  recipe_id INT,
  FOREIGN KEY (client_id) REFERENCES clients(id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(id)
);