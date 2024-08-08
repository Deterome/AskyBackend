SELECT * FROM collaborations_users
    INNER JOIN collaborations
        ON collaborations_users.collab_id = collaborations.collab_id
    INNER JOIN profiles
        ON collaborations_users.user_id = profiles.user_id;