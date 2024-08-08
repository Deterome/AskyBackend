CREATE TABLE profiles (
     profile_id SERIAL PRIMARY KEY,
     user_id INTEGER UNIQUE,
     bio VARCHAR(255),
     firstname VARCHAR(255),
     surname VARCHAR(255),
     birthday TIMESTAMP,
     avatar_url VARCHAR(255),
     rating BIGINT,
     FOREIGN KEY (user_id) REFERENCES users (user_id)
);
