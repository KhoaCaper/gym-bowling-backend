-- =============================================
-- Gym Bowling Database Setup Script (Complete ERD)
-- =============================================

-- Tạo database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'gym_bowling')
BEGIN
    CREATE DATABASE gym_bowling;
END
GO

USE gym_bowling;
GO

-- Create roles table
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
END
GO

-- Create users table (with role_id foreign key)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
BEGIN
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
    
    PRINT 'Table users created';
END
GO

-- Tạo bảng service_types
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='service_types' AND xtype='U')
BEGIN
    CREATE TABLE service_types (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        description NTEXT,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE()
    );
    
    PRINT 'Table service_types created';
END
GO

-- Create centers table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='centers' AND xtype='U')
BEGIN
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
    
    PRINT 'Table centers created';
END
GO



-- Create time_frames table (center operating hours)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='time_frames' AND xtype='U')
BEGIN
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
    
    PRINT 'Table time_frames created';
END
GO

-- Create services table (linked to centers)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='services' AND xtype='U')
BEGIN
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
    
    PRINT 'Table services created';
END
GO

-- Create package_plans table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='package_plans' AND xtype='U')
BEGIN
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
    
    PRINT 'Table package_plans created';
END
GO

-- Create package_plan_details table (package-service relationships)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='package_plan_details' AND xtype='U')
BEGIN
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
    
    PRINT 'Table package_plan_details created';
END
GO

-- Create orders table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='orders' AND xtype='U')
BEGIN
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
    
    PRINT 'Table orders created';
END
GO

-- Create order_packages table (order details with usage time)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='order_packages' AND xtype='U')
BEGIN
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
    
    PRINT 'Table order_packages created';
END
GO

-- Create payments table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='payments' AND xtype='U')
BEGIN
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
    
    PRINT 'Table payments created';
END
GO

-- Insert sample data

-- Sample Roles (must be first)
IF NOT EXISTS (SELECT * FROM roles)
BEGIN
    INSERT INTO roles (name, description) VALUES
    ('USER', 'Regular customer with basic access'),
    ('STAFF', 'Staff member with management access'),
    ('ADMIN', 'Administrator with full system access');
    
    PRINT 'Sample roles created';
END
GO

-- Sample Service Types
IF NOT EXISTS (SELECT * FROM service_types)
BEGIN
    INSERT INTO service_types (name, description) VALUES
    ('Gym', 'Gym fitness service'),
    ('Bowling', 'Bowling alley service'),
    ('Spa', 'Spa and wellness service'),
    ('Personal Training', 'Personal training service');
    
    PRINT 'Sample service types created';
END
GO

-- Sample Centers
IF NOT EXISTS (SELECT * FROM centers)
BEGIN
    INSERT INTO centers (name, address, phone, description) VALUES
    ('Gym Center A', '123 Main Street', '0123456789', 'Downtown fitness center'),
    ('Gym Center B', '456 Oak Avenue', '0987654321', 'Suburban wellness center'),
    ('Gym Center C', '789 Pine Road', '0111222333', 'Premium sports complex');
    
    PRINT 'Sample centers created';
END
GO



-- Sample Services (with center_id)
IF NOT EXISTS (SELECT * FROM services)
BEGIN
    INSERT INTO services (service_type_id, center_id, name, description, hourly_rate) VALUES
    -- Center A: Gym + Bowling
    (1, 1, 'Gym A1', 'Basic gym at Center A', 50000.00),
    (2, 1, 'Bowling A1', 'Bowling lanes at Center A', 100000.00),
    
    -- Center B: Gym + Bowling + Spa
    (1, 2, 'Gym B1', 'Premium gym at Center B', 60000.00),
    (2, 2, 'Bowling B1', 'VIP bowling at Center B', 120000.00),
    (3, 2, 'Spa B1', 'Wellness spa at Center B', 200000.00),
    
    -- Center C: All services
    (1, 3, 'Gym C1', 'Elite gym at Center C', 80000.00),
    (2, 3, 'Bowling C1', 'Championship bowling at Center C', 150000.00),
    (3, 3, 'Spa C1', 'Luxury spa at Center C', 350000.00),
    (4, 3, 'PT C1', 'Personal training at Center C', 300000.00);
    
    PRINT 'Sample services created';
END
GO

-- Sample Time Frames (giờ hoạt động của từng center)
IF NOT EXISTS (SELECT * FROM time_frames)
BEGIN
    INSERT INTO time_frames (center_id, start_time, end_time, day_of_week) VALUES
    -- Center A: Hoạt động 6h-22h thứ 2-6, 8h-20h cuối tuần
    (1, '06:00', '22:00', 'MONDAY'), (1, '06:00', '22:00', 'TUESDAY'), (1, '06:00', '22:00', 'WEDNESDAY'),
    (1, '06:00', '22:00', 'THURSDAY'), (1, '06:00', '22:00', 'FRIDAY'),
    (1, '08:00', '20:00', 'SATURDAY'), (1, '08:00', '20:00', 'SUNDAY'),
    
    -- Center B: Hoạt động 5h-23h thứ 2-6, 7h-21h cuối tuần  
    (2, '05:00', '23:00', 'MONDAY'), (2, '05:00', '23:00', 'TUESDAY'), (2, '05:00', '23:00', 'WEDNESDAY'),
    (2, '05:00', '23:00', 'THURSDAY'), (2, '05:00', '23:00', 'FRIDAY'),
    (2, '07:00', '21:00', 'SATURDAY'), (2, '07:00', '21:00', 'SUNDAY'),
    
    -- Center C: Hoạt động 24/7 (center cao cấp)
    (3, '00:00', '23:59', 'MONDAY'), (3, '00:00', '23:59', 'TUESDAY'), (3, '00:00', '23:59', 'WEDNESDAY'),
    (3, '00:00', '23:59', 'THURSDAY'), (3, '00:00', '23:59', 'FRIDAY'),
    (3, '00:00', '23:59', 'SATURDAY'), (3, '00:00', '23:59', 'SUNDAY');
    
    PRINT 'Sample time frames created';
END
GO

-- Sample Package Plans
IF NOT EXISTS (SELECT * FROM package_plans)
BEGIN
    INSERT INTO package_plans (name, description, price, duration_months, is_active) VALUES
    ('Basic Package', 'Basic gym access for 1 month', 500000.00, 1, 1),
    ('Standard Package', 'Gym + Bowling for 3 months', 1200000.00, 3, 1),
    ('Premium Package', 'All services for 6 months', 2500000.00, 6, 1),
    ('VIP Package', 'Premium gym + PT for 3 months', 1800000.00, 3, 1);
    
    PRINT 'Sample package plans created';
END
GO

-- Sample Package Plan Details (package-service relationships)
IF NOT EXISTS (SELECT * FROM package_plan_details)
BEGIN
    -- Basic Package: Gym A only
    INSERT INTO package_plan_details (package_plan_id, service_id, sessions_included) VALUES
    (1, 1, 30);
    
    -- Standard Package: Gym A + Bowling A
    INSERT INTO package_plan_details (package_plan_id, service_id, sessions_included) VALUES
    (2, 1, 60),
    (2, 3, 24);
    
    -- Premium Package: All services
    INSERT INTO package_plan_details (package_plan_id, service_id, sessions_included) VALUES
    (3, 2, 120),
    (3, 4, 48),
    (3, 5, 24),
    (3, 6, 12),
    (3, 7, 24);
    
    -- VIP Package: Premium Gym + PT
    INSERT INTO package_plan_details (package_plan_id, service_id, sessions_included) VALUES
    (4, 2, 60),
    (4, 7, 36);
    
    PRINT 'Sample package plan details created';
END
GO

-- Sample Users (with role_id)
IF NOT EXISTS (SELECT * FROM users)
BEGIN
    INSERT INTO users (firebase_uid, email, full_name, phone, role_id) VALUES
    ('admin-firebase-uid', 'admin@gym.com', 'Admin User', '0123456789', 3), -- ADMIN role_id = 3
    ('staff-firebase-uid', 'staff@gym.com', 'John Staff', '0987654321', 2), -- STAFF role_id = 2  
    ('user-firebase-uid', 'user@gym.com', 'Jane Customer', '0111222333', 1); -- USER role_id = 1
    
    PRINT 'Sample users created';
END
GO

-- Display database summary
SELECT 
    'Database gym_bowling is ready!' as Message,
    (SELECT COUNT(*) FROM users) as Total_Users,
    (SELECT COUNT(*) FROM service_types) as Total_ServiceTypes,
    (SELECT COUNT(*) FROM centers) as Total_Centers,
    (SELECT COUNT(*) FROM services) as Total_Services,
    (SELECT COUNT(*) FROM package_plans) as Total_Packages,
    (SELECT COUNT(*) FROM package_plan_details) as Total_PackageDetails,
    (SELECT COUNT(*) FROM time_frames) as Total_TimeFrames,
    (SELECT COUNT(*) FROM orders) as Total_Orders,
    (SELECT COUNT(*) FROM payments) as Total_Payments;

PRINT '=== DATABASE SETUP COMPLETED ===';
PRINT 'Database created with all tables according to ERD!';
PRINT 'Ready to run Spring Boot application!';
