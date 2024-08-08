SELECT * FROM questions
    INNER JOIN profiles
        ON questions.author = profiles.user_id;