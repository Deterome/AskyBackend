SELECT * FROM answers
    INNER JOIN questions
        ON answers.question_id = questions.question_id
    INNER JOIN profiles
        ON answers.author = profiles.user_id;