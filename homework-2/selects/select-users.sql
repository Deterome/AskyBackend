SELECT * FROM users 
    INNER JOIN roles
        ON users.role_id = roles.role_id;