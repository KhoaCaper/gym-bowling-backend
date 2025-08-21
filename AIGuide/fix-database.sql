-- =============================================
-- Fix Database Script - Add Role Table and Update Users
-- =============================================

USE gym_bowling;
GO

-- Step 1: Create roles table if not exists
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='roles' AND xtype='U')
BEGIN
    CREATE TABLE roles (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(50) NOT NULL UNIQUE,
        description NTEXT,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE()
    );
    
    CREATE INDEX IX_roles_name ON roles(name);
    PRINT 'Table roles created';
    
    -- Insert roles
    INSERT INTO roles (name, description) VALUES
    ('USER', 'Regular customer with basic access'),
    ('STAFF', 'Staff member with management access'),
    ('ADMIN', 'Administrator with full system access');
    
    PRINT 'Roles data inserted';
END
ELSE
BEGIN
    PRINT 'Table roles already exists';
END
GO

-- Step 2: Add role_id column to users table
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'users' AND COLUMN_NAME = 'role_id')
BEGIN
    -- Add role_id column
    ALTER TABLE users ADD role_id BIGINT;
    PRINT 'Added role_id column to users';
    
    -- Update existing users with role_id based on old role column
    UPDATE users SET role_id = 1 WHERE role = 'USER';
    UPDATE users SET role_id = 2 WHERE role = 'STAFF';  
    UPDATE users SET role_id = 3 WHERE role = 'ADMIN';
    PRINT 'Updated users with role_id values';
    
    -- Make role_id NOT NULL
    ALTER TABLE users ALTER COLUMN role_id BIGINT NOT NULL;
    PRINT 'Made role_id NOT NULL';
    
    -- Add foreign key constraint
    ALTER TABLE users ADD CONSTRAINT FK_users_role FOREIGN KEY (role_id) REFERENCES roles(id);
    PRINT 'Added foreign key constraint';
    
    -- Create index
    CREATE INDEX IX_users_role ON users(role_id);
    PRINT 'Created index on role_id';
    
    -- Drop old role column
    ALTER TABLE users DROP CONSTRAINT CK_users_role; -- Drop check constraint first
    ALTER TABLE users DROP COLUMN role; -- Drop old role column
    PRINT 'Dropped old role column and constraint';
END
ELSE
BEGIN
    PRINT 'Column role_id already exists in users table';
END
GO

-- Step 3: Verify the fix
SELECT 
    'Database fix completed!' as Message,
    (SELECT COUNT(*) FROM roles) as Total_Roles,
    (SELECT COUNT(*) FROM users) as Total_Users;

-- Show users with roles
SELECT 
    u.full_name,
    u.email,
    r.name as role_name
FROM users u
JOIN roles r ON u.role_id = r.id;

PRINT '=== DATABASE FIX COMPLETED ===';
PRINT 'Users table now has role_id foreign key!';
PRINT 'Ready to test Spring Boot application!';
