CREATE TABLE questions (
     question_id SERIAL PRIMARY KEY,
     header VARCHAR(255),
     body VARCHAR(5000),
     author INTEGER,
     interesting BIGINT,
     create_time TIMESTAMP,
     FOREIGN KEY (author) REFERENCES profiles (user_id)
);
