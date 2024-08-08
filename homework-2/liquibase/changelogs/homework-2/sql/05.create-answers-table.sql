CREATE TABLE answers (
     answer_id SERIAL PRIMARY KEY,
     body VARCHAR(5000),
     author INTEGER,
     question_id INTEGER,
     usefulness BIGINT,
     create_time TIMESTAMP,
     FOREIGN KEY (question_id) REFERENCES questions (question_id),
     FOREIGN KEY (author) REFERENCES profiles (user_id)
);
