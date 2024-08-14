CREATE TABLE collaborations_users (
     join_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     collab_id UUID,
     user_id UUID,
     join_date TIMESTAMP,
     FOREIGN KEY (collab_id) REFERENCES collaborations (collab_id),
     FOREIGN KEY (user_id) REFERENCES profiles (user_id)
);
