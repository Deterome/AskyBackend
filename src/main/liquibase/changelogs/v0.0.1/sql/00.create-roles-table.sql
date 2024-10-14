CREATE TABLE roles (
     role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     role_name VARCHAR(127) UNIQUE
);
