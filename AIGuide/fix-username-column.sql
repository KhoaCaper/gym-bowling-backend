-- Fix: Add username column to users table
USE gym_bowling;
GO

-- Add username column
ALTER TABLE users ADD username NVARCHAR(255);
PRINT 'Username column added';
GO

-- Update existing users with usernames
UPDATE users SET username = 'admin' WHERE email = 'admin@gym.com';
UPDATE users SET username = 'staff' WHERE email = 'staff@gym.com';  
UPDATE users SET username = 'user' WHERE email = 'user@gym.com';
PRINT 'Existing users updated with usernames';
GO

-- Make username NOT NULL and UNIQUE
ALTER TABLE users ALTER COLUMN username NVARCHAR(255) NOT NULL;
ALTER TABLE users ADD CONSTRAINT UK_users_username UNIQUE (username);
PRINT 'Username constraints added';
GO

-- Verify
SELECT username, email, full_name FROM users;
PRINT 'Database updated successfully!';
