CREATE TABLE users (
     user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     role_id INTEGER,
     username VARCHAR(255),
     hashed_password VARCHAR(255),
     FOREIGN KEY (role_id) REFERENCES roles (role_id)
);
