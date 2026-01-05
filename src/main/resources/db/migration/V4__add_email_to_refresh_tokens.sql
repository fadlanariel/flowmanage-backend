ALTER TABLE refresh_tokens
ADD COLUMN email VARCHAR(255);

UPDATE refresh_tokens rt
SET email = u.email
FROM users u
WHERE rt.user_id = u.id;
