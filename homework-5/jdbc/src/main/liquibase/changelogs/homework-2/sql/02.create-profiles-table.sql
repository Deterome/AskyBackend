CREATE TABLE profiles (
     profile_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     user_id UUID UNIQUE,
     bio VARCHAR(255),
     firstname VARCHAR(255),
     surname VARCHAR(255),
     birthday TIMESTAMP,
     avatar_url VARCHAR(2083),
     rating BIGINT,
     FOREIGN KEY (user_id) REFERENCES users (user_id)
);
