-- =============================================
-- Recreate Database from Scratch
-- =============================================

-- Drop database completely
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'gym_bowling')
BEGIN
    ALTER DATABASE gym_bowling SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE gym_bowling;
    PRINT 'Database gym_bowling dropped';
END
GO

-- Create fresh database
CREATE DATABASE gym_bowling;
PRINT 'Database gym_bowling created';
GO

USE gym_bowling;
GO

-- Create roles table FIRST
CREATE TABLE roles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL UNIQUE,
    description NTEXT,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
PRINT 'Table roles created';
GO

-- Insert roles immediately
INSERT INTO roles (name, description) VALUES
('USER', 'Regular customer'),
('STAFF', 'Staff member'),
('ADMIN', 'Administrator');
PRINT 'Roles inserted';
GO

-- Create users table with role_id
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    firebase_uid NVARCHAR(255) NOT NULL UNIQUE,
    email NVARCHAR(255) NOT NULL UNIQUE,
    full_name NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20),
    role_id BIGINT NOT NULL,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    
    CONSTRAINT FK_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
);
PRINT 'Table users created';
GO

-- Create other tables
CREATE TABLE service_types (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description NTEXT,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
PRINT 'Table service_types created';
GO

CREATE TABLE centers (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    address NVARCHAR(500),
    phone NVARCHAR(20),
    description NTEXT,
    is_active BIT NOT NULL DEFAULT 1,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
PRINT 'Table centers created';
GO

CREATE TABLE services (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    service_type_id BIGINT NOT NULL,
    center_id BIGINT NOT NULL,
    name NVARCHAR(255) NOT NULL,
    description NTEXT,
    hourly_rate DECIMAL(10,2),
    is_active BIT NOT NULL DEFAULT 1,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    
    CONSTRAINT FK_services_service_type FOREIGN KEY (service_type_id) REFERENCES service_types(id),
    CONSTRAINT FK_services_center FOREIGN KEY (center_id) REFERENCES centers(id)
);
PRINT 'Table services created';
GO

CREATE TABLE package_plans (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description NTEXT,
    price DECIMAL(10,2) NOT NULL,
    duration_months INT NOT NULL,
    is_active BIT NOT NULL DEFAULT 1,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
PRINT 'Table package_plans created';
GO

CREATE TABLE orders (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'PENDING',
    order_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    
    CONSTRAINT FK_orders_user FOREIGN KEY (user_id) REFERENCES users(id)
);
PRINT 'Table orders created';
GO

CREATE TABLE payments (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method NVARCHAR(20) NOT NULL DEFAULT 'VNPAY',
    transaction_id NVARCHAR(255),
    status NVARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    
    CONSTRAINT FK_payments_order FOREIGN KEY (order_id) REFERENCES orders(id)
);
PRINT 'Table payments created';
GO

-- Insert sample data
INSERT INTO service_types (name, description) VALUES
('Gym', 'Gym fitness'),
('Bowling', 'Bowling alley'),
('Spa', 'Spa wellness'),
('PT', 'Personal training');
PRINT 'Service types inserted';
GO

INSERT INTO centers (name, address, phone, description) VALUES
('Center A', '123 Street A', '0123456789', 'Downtown center'),
('Center B', '456 Street B', '0987654321', 'Suburban center'),
('Center C', '789 Street C', '0111222333', 'Premium center');
PRINT 'Centers inserted';
GO

INSERT INTO services (service_type_id, center_id, name, description, hourly_rate) VALUES
(1, 1, 'Gym A1', 'Basic gym at Center A', 50000),
(2, 1, 'Bowling A1', 'Bowling at Center A', 100000),
(1, 2, 'Gym B1', 'Premium gym at Center B', 60000),
(2, 2, 'Bowling B1', 'VIP bowling at Center B', 120000),
(3, 2, 'Spa B1', 'Spa at Center B', 200000),
(1, 3, 'Gym C1', 'Elite gym at Center C', 80000),
(2, 3, 'Bowling C1', 'Championship bowling at Center C', 150000),
(3, 3, 'Spa C1', 'Luxury spa at Center C', 350000),
(4, 3, 'PT C1', 'Personal training at Center C', 300000);
PRINT 'Services inserted';
GO

INSERT INTO package_plans (name, description, price, duration_months) VALUES
('Basic Package', 'Basic gym access', 500000, 1),
('Standard Package', 'Gym + Bowling', 1200000, 3),
('Premium Package', 'All services', 2500000, 6),
('VIP Package', 'Premium + PT', 1800000, 3);
PRINT 'Package plans inserted';
GO

-- Insert users with correct role_id
INSERT INTO users (firebase_uid, email, full_name, phone, role_id) VALUES
('admin-firebase-uid', 'admin@gym.com', 'Admin User', '0123456789', 3),
('staff-firebase-uid', 'staff@gym.com', 'John Staff', '0987654321', 2),
('user-firebase-uid', 'user@gym.com', 'Jane Customer', '0111222333', 1);
PRINT 'Users inserted';
GO

-- Final verification
SELECT 
    u.full_name,
    u.email, 
    r.name as role
FROM users u
JOIN roles r ON u.role_id = r.id;

PRINT 'Database setup completed successfully!';
PRINT 'All tables created with proper relationships!';
