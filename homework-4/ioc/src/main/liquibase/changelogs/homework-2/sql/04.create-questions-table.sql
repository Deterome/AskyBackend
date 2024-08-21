CREATE TABLE questions (
     question_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     header VARCHAR(255),
     body VARCHAR,
     author UUID,
     interesting BIGINT,
     create_time TIMESTAMP,
     FOREIGN KEY (author) REFERENCES profiles (user_id)
);
