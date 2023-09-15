CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  login VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  birthday DATE NOT NULL
);



CREATE TABLE IF NOT EXISTS list_friends (
  id_user INT,
  id_friend INT,
  user_friends INT NOT NULL,
  CONSTRAINT list_friends_fk_user FOREIGN KEY (id_user) REFERENCES users(id),
  CONSTRAINT list_friends_fk_friend FOREIGN KEY (id_friend) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS reating (
  reating_id INT PRIMARY KEY,
  name VARCHAR(255),
  description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS genre (
  genre_id INT PRIMARY KEY,
  name_genre VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS films (
  films_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  releasedate DATE,
  duration INT,
  rating INT,
  genre_id INT,
  CONSTRAINT films_fk_genre FOREIGN KEY (genre_id) REFERENCES genre(genre_id),
  CONSTRAINT films_fk_rating FOREIGN KEY (rating) REFERENCES reating(reating_id)
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id INT,
  genre_id INT,
  CONSTRAINT film_genre_fk_film FOREIGN KEY (film_id) REFERENCES films(films_id),
  CONSTRAINT film_genre_fk_genre FOREIGN KEY (genre_id) REFERENCES genre(genre_id)
);

CREATE TABLE IF NOT EXISTS users_like (
  id_user INT,
  id_films INT,
  CONSTRAINT users_like_fk_user FOREIGN KEY (id_user) REFERENCES users(id),
  CONSTRAINT users_like_fk_films FOREIGN KEY (id_films) REFERENCES films(films_id)
);