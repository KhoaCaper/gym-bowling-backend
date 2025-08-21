-- Add username column to existing users table
USE gym_bowling;
GO

-- Add username column
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'users' AND COLUMN_NAME = 'username')
BEGIN
    ALTER TABLE users ADD username NVARCHAR(255);
    PRINT 'Username column added';
    
    -- Update existing users with temporary usernames
    UPDATE users SET username = 'admin' WHERE email = 'admin@gym.com';
    UPDATE users SET username = 'staff' WHERE email = 'staff@gym.com';  
    UPDATE users SET username = 'user' WHERE email = 'user@gym.com';
    PRINT 'Existing users updated with usernames';
    
    -- Make username NOT NULL and UNIQUE
    ALTER TABLE users ALTER COLUMN username NVARCHAR(255) NOT NULL;
    ALTER TABLE users ADD CONSTRAINT UK_users_username UNIQUE (username);
    PRINT 'Username constraints added';
END
ELSE
BEGIN
    PRINT 'Username column already exists';
END
GO

-- Verify
SELECT username, email, full_name FROM users;
PRINT 'Users table updated successfully!';
