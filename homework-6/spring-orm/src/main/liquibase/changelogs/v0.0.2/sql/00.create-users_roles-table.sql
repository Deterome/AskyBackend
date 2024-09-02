CREATE TABLE roles (
     user_id UUID REFERENCES users(user_id),
     role_id UUID REFERENCES roles(role_id),
     PRIMARY KEY(user_id, role_id)
);
