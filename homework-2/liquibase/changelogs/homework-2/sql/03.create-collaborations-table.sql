CREATE TABLE collaborations (
     collab_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     collab_name VARCHAR(255),
     create_time TIMESTAMP
);
