CREATE TABLE collaborations_users (
     join_id SERIAL PRIMARY KEY,
     collab_id INTEGER,
     user_id INTEGER,
     join_date TIMESTAMP,
     FOREIGN KEY (collab_id) REFERENCES collaborations (collab_id),
     FOREIGN KEY (user_id) REFERENCES profiles (user_id)
);
