SELECT * FROM profiles
    INNER JOIN users 
        ON profiles.user_id = users.user_id;