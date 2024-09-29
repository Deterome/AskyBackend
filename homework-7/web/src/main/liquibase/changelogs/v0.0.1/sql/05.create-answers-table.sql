CREATE TABLE answers (
     answer_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     body VARCHAR,
     author UUID,
     question_id UUID,
     usefulness BIGINT,
     create_time TIMESTAMP,
     FOREIGN KEY (question_id) REFERENCES questions (question_id),
     FOREIGN KEY (author) REFERENCES users (user_id)
);
