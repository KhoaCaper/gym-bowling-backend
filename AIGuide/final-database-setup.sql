-- =============================================
-- FINAL COMPLETE DATABASE SETUP SCRIPT
-- Gym Bowling Backend - All Tables with Relationships
-- =============================================

-- Force close all connections and drop database
USE master;
GO

-- Kill all connections to gym_bowling database
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'gym_bowling')
BEGIN
    PRINT 'Killing active connections to gym_bowling database...';
    
    DECLARE @sql NVARCHAR(MAX) = '';
    SELECT @sql = @sql + 'KILL ' + CAST(session_id AS VARCHAR(10)) + ';'
    FROM sys.dm_exec_sessions 
    WHERE database_id = DB_ID('gym_bowling');
    
    IF LEN(@sql) > 0
    BEGIN
        EXEC sp_executesql @sql;
        PRINT 'Active connections killed';
    END
    
    -- Drop database
    DROP DATABASE gym_bowling;
    PRINT 'Existing database gym_bowling dropped';
END
ELSE
BEGIN
    PRINT 'Database gym_bowling does not exist';
END
GO

-- Wait a moment for cleanup
WAITFOR DELAY '00:00:02';
GO

-- Create new database
CREATE DATABASE gym_bowling;
PRINT 'New database gym_bowling created';
GO

USE gym_bowling;
GO

-- =============================================
-- CREATE ALL TABLES IN CORRECT ORDER
-- =============================================

-- 1. Create roles table (independent)
CREATE TABLE roles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL UNIQUE,
    description NTEXT,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
CREATE INDEX IX_roles_name ON roles(name);
PRINT '1. Table roles created';
GO

-- 2. Create service_types table (independent)
CREATE TABLE service_types (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description NTEXT,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
PRINT '2. Table service_types created';
GO

-- 3. Create centers table (independent)
CREATE TABLE centers (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    address NVARCHAR(500),
    phone NVARCHAR(20),
    description NTEXT,
    is_active BIT NOT NULL DEFAULT 1,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
CREATE INDEX IX_centers_active ON centers(is_active);
PRINT '3. Table centers created';
GO

-- 4. Create users table (depends on roles)
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
CREATE INDEX IX_users_firebase_uid ON users(firebase_uid);
CREATE INDEX IX_users_email ON users(email);
CREATE INDEX IX_users_role ON users(role_id);
PRINT '4. Table users created';
GO

-- 5. Create time_frames table (depends on centers)
CREATE TABLE time_frames (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    center_id BIGINT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    day_of_week NVARCHAR(20) NOT NULL,
    is_available BIT NOT NULL DEFAULT 1,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    
    CONSTRAINT FK_time_frames_center FOREIGN KEY (center_id) REFERENCES centers(id),
    CONSTRAINT CK_time_frames_day CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'))
);
CREATE INDEX IX_time_frames_center ON time_frames(center_id);
CREATE INDEX IX_time_frames_available ON time_frames(is_available);
PRINT '5. Table time_frames created';
GO

-- 6. Create services table (depends on service_types and centers)
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
CREATE INDEX IX_services_type ON services(service_type_id);
CREATE INDEX IX_services_center ON services(center_id);
CREATE INDEX IX_services_active ON services(is_active);
PRINT '6. Table services created';
GO

-- 7. Create package_plans table (independent)
CREATE TABLE package_plans (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description NTEXT,
    price DECIMAL(10,2) NOT NULL,
    duration_months INT NOT NULL,
    is_active BIT NOT NULL DEFAULT 1,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
CREATE INDEX IX_package_plans_active ON package_plans(is_active);
PRINT '7. Table package_plans created';
GO

-- 8. Create package_plan_details table (depends on package_plans and services)
CREATE TABLE package_plan_details (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    package_plan_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    sessions_included INT NOT NULL DEFAULT 0,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    
    CONSTRAINT FK_package_details_package FOREIGN KEY (package_plan_id) REFERENCES package_plans(id) ON DELETE CASCADE,
    CONSTRAINT FK_package_details_service FOREIGN KEY (service_id) REFERENCES services(id)
);
CREATE INDEX IX_package_details_package ON package_plan_details(package_plan_id);
CREATE INDEX IX_package_details_service ON package_plan_details(service_id);
PRINT '8. Table package_plan_details created';
GO

-- 9. Create orders table (depends on users)
CREATE TABLE orders (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'PENDING',
    order_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    
    CONSTRAINT FK_orders_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT CK_orders_status CHECK (status IN ('PENDING', 'PAID', 'CANCELLED', 'COMPLETED'))
);
CREATE INDEX IX_orders_user ON orders(user_id);
CREATE INDEX IX_orders_status ON orders(status);
CREATE INDEX IX_orders_date ON orders(order_date);
PRINT '9. Table orders created';
GO

-- 10. Create order_packages table (depends on orders and package_plans)
CREATE TABLE order_packages (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    package_plan_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    start_time DATETIME2,
    end_time DATETIME2,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    
    CONSTRAINT FK_order_packages_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT FK_order_packages_package FOREIGN KEY (package_plan_id) REFERENCES package_plans(id)
);
CREATE INDEX IX_order_packages_order ON order_packages(order_id);
CREATE INDEX IX_order_packages_package ON order_packages(package_plan_id);
CREATE INDEX IX_order_packages_time ON order_packages(start_time, end_time);
PRINT '10. Table order_packages created';
GO

-- 11. Create payments table (depends on orders)
CREATE TABLE payments (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method NVARCHAR(20) NOT NULL DEFAULT 'VNPAY',
    transaction_id NVARCHAR(255),
    status NVARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    vnpay_response NTEXT,
    
    CONSTRAINT FK_payments_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT CK_payments_method CHECK (payment_method IN ('VNPAY', 'CASH', 'BANK_TRANSFER')),
    CONSTRAINT CK_payments_status CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED', 'CANCELLED'))
);
CREATE INDEX IX_payments_order ON payments(order_id);
CREATE INDEX IX_payments_transaction ON payments(transaction_id);
CREATE INDEX IX_payments_status ON payments(status);
PRINT '11. Table payments created';
GO

-- =============================================
-- INSERT SAMPLE DATA IN CORRECT ORDER
-- =============================================

-- Insert roles first (required by users)
INSERT INTO roles (name, description) VALUES
('USER', 'Regular customer with basic access'),
('STAFF', 'Staff member with management access'),
('ADMIN', 'Administrator with full system access');
PRINT 'Sample roles inserted';
GO

-- Insert service types
INSERT INTO service_types (name, description) VALUES
('Gym', 'Gym fitness service'),
('Bowling', 'Bowling alley service'),
('Spa', 'Spa and wellness service'),
('Personal Training', 'Personal training service');
PRINT 'Sample service types inserted';
GO

-- Insert centers
INSERT INTO centers (name, address, phone, description) VALUES
('Center A', '123 Street A', '0123456789', 'Downtown center'),
('Center B', '456 Street B', '0987654321', 'Suburban center'),
('Center C', '789 Street C', '0111222333', 'Premium center');
PRINT 'Sample centers inserted';
GO

-- Insert users (with role_id references)
INSERT INTO users (firebase_uid, email, full_name, phone, role_id) VALUES
('admin-firebase-uid', 'admin@gym.com', 'Admin User', '0123456789', 3), -- ADMIN
('staff-firebase-uid', 'staff@gym.com', 'John Staff', '0987654321', 2), -- STAFF
('user-firebase-uid', 'user@gym.com', 'Jane Customer', '0111222333', 1); -- USER
PRINT 'Sample users inserted';
GO

-- Insert time frames (center operating hours)
INSERT INTO time_frames (center_id, start_time, end_time, day_of_week) VALUES
-- Center A: 6AM-10PM weekdays, 8AM-8PM weekends
(1, '06:00', '22:00', 'MONDAY'), (1, '06:00', '22:00', 'TUESDAY'), (1, '06:00', '22:00', 'WEDNESDAY'),
(1, '06:00', '22:00', 'THURSDAY'), (1, '06:00', '22:00', 'FRIDAY'),
(1, '08:00', '20:00', 'SATURDAY'), (1, '08:00', '20:00', 'SUNDAY'),

-- Center B: Extended hours
(2, '05:00', '23:00', 'MONDAY'), (2, '05:00', '23:00', 'TUESDAY'), (2, '05:00', '23:00', 'WEDNESDAY'),
(2, '05:00', '23:00', 'THURSDAY'), (2, '05:00', '23:00', 'FRIDAY'),
(2, '07:00', '21:00', 'SATURDAY'), (2, '07:00', '21:00', 'SUNDAY'),

-- Center C: 24/7 premium
(3, '00:00', '23:59', 'MONDAY'), (3, '00:00', '23:59', 'TUESDAY'), (3, '00:00', '23:59', 'WEDNESDAY'),
(3, '00:00', '23:59', 'THURSDAY'), (3, '00:00', '23:59', 'FRIDAY'),
(3, '00:00', '23:59', 'SATURDAY'), (3, '00:00', '23:59', 'SUNDAY');
PRINT 'Sample time frames inserted';
GO

-- Insert services (depends on service_types and centers)
INSERT INTO services (service_type_id, center_id, name, description, hourly_rate) VALUES
-- Center A: Basic services
(1, 1, 'Gym A1', 'Basic gym at Center A', 50000.00),
(2, 1, 'Bowling A1', 'Bowling at Center A', 100000.00),

-- Center B: More services
(1, 2, 'Gym B1', 'Premium gym at Center B', 60000.00),
(2, 2, 'Bowling B1', 'VIP bowling at Center B', 120000.00),
(3, 2, 'Spa B1', 'Wellness spa at Center B', 200000.00),

-- Center C: All services
(1, 3, 'Gym C1', 'Elite gym at Center C', 80000.00),
(2, 3, 'Bowling C1', 'Championship bowling at Center C', 150000.00),
(3, 3, 'Spa C1', 'Luxury spa at Center C', 350000.00),
(4, 3, 'PT C1', 'Personal training at Center C', 300000.00);
PRINT 'Sample services inserted';
GO

-- Insert package plans
INSERT INTO package_plans (name, description, price, duration_months, is_active) VALUES
('Basic Package', 'Basic gym access for 1 month', 500000.00, 1, 1),
('Standard Package', 'Gym + Bowling for 3 months', 1200000.00, 3, 1),
('Premium Package', 'All services for 6 months', 2500000.00, 6, 1),
('VIP Package', 'Premium gym + PT for 3 months', 1800000.00, 3, 1);
PRINT 'Sample package plans inserted';
GO

-- Insert package plan details (depends on package_plans and services)
INSERT INTO package_plan_details (package_plan_id, service_id, sessions_included) VALUES
-- Basic Package: Only Gym A1
(1, 1, 30),

-- Standard Package: Gym A1 + Bowling A1
(2, 1, 60),
(2, 2, 24),

-- Premium Package: Multiple services
(3, 3, 120), -- Gym B1
(3, 4, 48),  -- Bowling B1
(3, 5, 24),  -- Spa B1

-- VIP Package: Premium services
(4, 6, 60),  -- Gym C1
(4, 9, 36);  -- PT C1
PRINT 'Sample package plan details inserted';
GO

-- =============================================
-- VERIFY COMPLETE DATABASE
-- =============================================

-- Show table counts
SELECT 
    'DATABASE CREATION COMPLETED!' as Status,
    (SELECT COUNT(*) FROM roles) as Roles,
    (SELECT COUNT(*) FROM users) as Users,
    (SELECT COUNT(*) FROM service_types) as ServiceTypes,
    (SELECT COUNT(*) FROM centers) as Centers,
    (SELECT COUNT(*) FROM services) as Services,
    (SELECT COUNT(*) FROM package_plans) as PackagePlans,
    (SELECT COUNT(*) FROM package_plan_details) as PackageDetails,
    (SELECT COUNT(*) FROM time_frames) as TimeFrames,
    (SELECT COUNT(*) FROM orders) as Orders,
    (SELECT COUNT(*) FROM payments) as Payments;

-- Show users with their roles
PRINT '=== USERS WITH ROLES ===';
SELECT 
    u.full_name as Name,
    u.email as Email,
    r.name as Role
FROM users u
JOIN roles r ON u.role_id = r.id
ORDER BY r.id;

-- Show centers with their services
PRINT '=== CENTERS WITH SERVICES ===';
SELECT 
    c.name as Center,
    st.name as ServiceType,
    s.name as Service,
    FORMAT(s.hourly_rate, 'N0') + ' VND' as Rate
FROM centers c
JOIN services s ON c.id = s.center_id
JOIN service_types st ON s.service_type_id = st.id
ORDER BY c.name, st.name;

-- Show packages with their services
PRINT '=== PACKAGES WITH SERVICES ===';
SELECT 
    pp.name as Package,
    FORMAT(pp.price, 'N0') + ' VND' as Price,
    pp.duration_months as Months,
    s.name as Service,
    ppd.sessions_included as Sessions
FROM package_plans pp
JOIN package_plan_details ppd ON pp.id = ppd.package_plan_id
JOIN services s ON ppd.service_id = s.id
ORDER BY pp.id, s.name;

PRINT '=== DATABASE SETUP COMPLETED SUCCESSFULLY ===';
PRINT 'All tables created with proper relationships!';
PRINT 'Sample data inserted and verified!';
PRINT 'Ready to run Spring Boot application!';
PRINT '';
PRINT 'Next steps:';
PRINT '1. Update User entity to use Role entity';
PRINT '2. Update UserService for Role relationships';
PRINT '3. Test Spring Boot application';
PRINT '4. Test Firebase authentication';
PRINT '5. Test APIs via Swagger or HTML';
GO
