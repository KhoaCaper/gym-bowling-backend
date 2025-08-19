-- =====================================================
-- GYM BOWLING BACKEND DATABASE SCRIPT
-- SQL Server Version
-- =====================================================
--
-- IMPORTANT NOTES:
-- 1. VNPAY SANDBOX CONFIGURATION:
--    - Use sandbox URLs for testing
--    - Test card numbers: 9704000000000018, 9704000000000026
--    - Test OTP: 123456
--    - Sandbox environment for development only
--
-- 2. FIREBASE AUTHENTICATION:
--    - Users authenticate via Firebase UID
--    - Firebase UID stored in users.firebase_uid field
--    - Role-based access control (ADMIN, STAFF, USER)
--    - Admin user created with firebase_uid: 'admin_firebase_uid'
--
-- 3. CENTER STRUCTURE:
--    - Center A: Gym-focused services
--    - Center B: Bowling-focused services  
--    - Center C: Combined gym + bowling services
--
-- =====================================================

USE master;
GO

-- Drop database if exists
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'gym_bowling')
BEGIN
    ALTER DATABASE gym_bowling SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE gym_bowling;
END
GO

-- Create new database
CREATE DATABASE gym_bowling;
GO

USE gym_bowling;
GO

-- =====================================================
-- CREATE TABLES
-- =====================================================

-- 1. ROLES TABLE
CREATE TABLE roles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL UNIQUE,
    description NVARCHAR(500),
    created_at DATETIME2 DEFAULT GETDATE()
);

-- 2. USERS TABLE
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    firebase_uid NVARCHAR(128) NOT NULL UNIQUE,
    email NVARCHAR(255) NOT NULL UNIQUE,
    full_name NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20),
    role_id BIGINT NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 3. CENTERS TABLE
CREATE TABLE centers (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL UNIQUE,
    address NVARCHAR(500),
    phone NVARCHAR(20),
    email NVARCHAR(255),
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE()
);

-- 4. SERVICE_TYPES TABLE
CREATE TABLE service_types (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(500),
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE()
);

-- 5. SERVICES TABLE
CREATE TABLE services (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description NVARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    center_id BIGINT NOT NULL,
    service_type_id BIGINT NOT NULL,
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (center_id) REFERENCES centers(id),
    FOREIGN KEY (service_type_id) REFERENCES service_types(id)
);

-- 6. TIME_FRAMES TABLE
CREATE TABLE time_frames (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    center_id BIGINT NOT NULL,
    day_of_week NVARCHAR(20) NOT NULL, -- MONDAY, TUESDAY, etc.
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_available BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (center_id) REFERENCES centers(id)
);

-- 7. PACKAGE_PLANS TABLE
CREATE TABLE package_plans (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description NVARCHAR(1000),
    price DECIMAL(10,2) NOT NULL,
    duration_months INT NOT NULL,
    center_id BIGINT NOT NULL,
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (center_id) REFERENCES centers(id)
);

-- 8. PACKAGE_PLAN_DETAILS TABLE
CREATE TABLE package_plan_details (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    package_plan_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    sessions_included INT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (package_plan_id) REFERENCES package_plans(id),
    FOREIGN KEY (service_id) REFERENCES services(id)
);

-- 9. ORDERS TABLE
CREATE TABLE orders (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status NVARCHAR(50) DEFAULT 'PENDING', -- PENDING, PAID, CANCELLED, COMPLETED
    order_date DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 10. ORDER_PACKAGES TABLE
CREATE TABLE order_packages (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    package_plan_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    start_time DATETIME2,
    end_time DATETIME2,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (package_plan_id) REFERENCES package_plans(id)
);

-- 11. PAYMENTS TABLE
CREATE TABLE payments (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    payment_method NVARCHAR(50) DEFAULT 'VNPAY',
    status NVARCHAR(50) DEFAULT 'PENDING', -- PENDING, SUCCESS, FAILED
    transaction_id NVARCHAR(255),
    vnpay_response NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- =====================================================
-- CREATE INDEXES FOR PERFORMANCE
-- =====================================================

-- Users indexes
CREATE INDEX idx_users_firebase_uid ON users(firebase_uid);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role_id ON users(role_id);

-- Services indexes
CREATE INDEX idx_services_center_id ON services(center_id);
CREATE INDEX idx_services_service_type_id ON services(service_type_id);
CREATE INDEX idx_services_active ON services(is_active);

-- Time frames indexes
CREATE INDEX idx_timeframes_center_id ON time_frames(center_id);
CREATE INDEX idx_timeframes_day_available ON time_frames(day_of_week, is_available);

-- Package plans indexes
CREATE INDEX idx_package_plans_center_id ON package_plans(center_id);
CREATE INDEX idx_package_plans_active ON package_plans(is_active);

-- Orders indexes
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_date ON orders(order_date);

-- Order packages indexes
CREATE INDEX idx_order_packages_order_id ON order_packages(order_id);
CREATE INDEX idx_order_packages_package_plan_id ON order_packages(package_plan_id);

-- Payments indexes
CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);

-- =====================================================
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert default roles
INSERT INTO roles (name, description) VALUES
('ADMIN', 'System administrator with full access'),
('STAFF', 'Staff member with center management access'),
('USER', 'Regular user with basic access');

-- Insert sample centers
INSERT INTO centers (name, address, phone, email) VALUES
('Center A', '123 Main Street, District 1, HCMC', '0901234567', 'centera@example.com'),
('Center B', '456 Business Avenue, District 3, HCMC', '0901234568', 'centerb@example.com'),
('Center C', '789 Central Road, District 1, HCMC', '0901234569', 'centerc@example.com');

-- Insert service types
INSERT INTO service_types (name, description) VALUES
('GYM', 'Gym and fitness services'),
('BOWLING', 'Bowling and entertainment services');

-- Insert sample services
INSERT INTO services (name, description, price, center_id, service_type_id) VALUES
-- Center A (Gym Focus) services
('Gym Membership - Monthly', 'Monthly gym membership with full access', 500000.00, 1, 1),
('Gym Membership - 3 Months', '3-month gym membership with discount', 1400000.00, 1, 1),
('Personal Training', 'One-on-one personal training session', 300000.00, 1, 1),

-- Center B (Bowling Focus) services
('Bowling Lane - Hourly', 'Bowling lane rental per hour', 200000.00, 2, 2),
('Bowling Package - 2 Hours', '2-hour bowling package with shoes', 350000.00, 2, 2),
('Bowling Tournament Entry', 'Entry fee for bowling tournament', 500000.00, 2, 2),

-- Center C (Combined) services
('Gym + Bowling Combo', 'Combined gym and bowling access', 800000.00, 3, 1),
('Fitness Class + Bowling', 'Fitness class followed by bowling', 400000.00, 3, 1);

-- Insert sample time frames
INSERT INTO time_frames (center_id, day_of_week, start_time, end_time) VALUES
-- Center A (Gym Focus) - Monday to Sunday
(1, 'MONDAY', '06:00:00', '22:00:00'),
(1, 'TUESDAY', '06:00:00', '22:00:00'),
(1, 'WEDNESDAY', '06:00:00', '22:00:00'),
(1, 'THURSDAY', '06:00:00', '22:00:00'),
(1, 'FRIDAY', '06:00:00', '22:00:00'),
(1, 'SATURDAY', '07:00:00', '21:00:00'),
(1, 'SUNDAY', '07:00:00', '21:00:00'),

-- Center B (Bowling Focus) - Monday to Sunday
(2, 'MONDAY', '10:00:00', '23:00:00'),
(2, 'TUESDAY', '10:00:00', '23:00:00'),
(2, 'WEDNESDAY', '10:00:00', '23:00:00'),
(2, 'THURSDAY', '10:00:00', '23:00:00'),
(2, 'FRIDAY', '10:00:00', '00:00:00'),
(2, 'SATURDAY', '09:00:00', '00:00:00'),
(2, 'SUNDAY', '09:00:00', '23:00:00'),

-- Center C (Combined) - Monday to Sunday
(3, 'MONDAY', '06:00:00', '23:00:00'),
(3, 'TUESDAY', '06:00:00', '23:00:00'),
(3, 'WEDNESDAY', '06:00:00', '23:00:00'),
(3, 'THURSDAY', '06:00:00', '23:00:00'),
(3, 'FRIDAY', '06:00:00', '00:00:00'),
(3, 'SATURDAY', '07:00:00', '00:00:00'),
(3, 'SUNDAY', '07:00:00', '23:00:00');

-- Insert sample package plans
INSERT INTO package_plans (name, description, price, duration_months, center_id) VALUES
-- Center A (Gym Focus) packages
('Gym Basic Monthly', 'Basic gym access for 1 month', 500000.00, 1, 1),
('Gym Premium 3 Months', 'Premium gym access for 3 months with personal training', 2000000.00, 3, 1),
('Gym VIP 6 Months', 'VIP gym access for 6 months with all amenities', 3500000.00, 6, 1),

-- Center B (Bowling Focus) packages
('Bowling Starter Pack', '10 bowling sessions within 1 month', 1500000.00, 1, 2),
('Bowling Pro Pack', '20 bowling sessions within 2 months', 2500000.00, 2, 2),
('Bowling Unlimited Monthly', 'Unlimited bowling for 1 month', 3000000.00, 1, 2),

-- Center C (Combined) packages
('Fitness + Bowling Combo', 'Gym access + 5 bowling sessions per month', 2500000.00, 1, 3),
('Premium Sports Package', 'Full gym access + unlimited bowling + fitness classes', 5000000.00, 3, 3);

-- Insert package plan details
INSERT INTO package_plan_details (package_plan_id, service_id, sessions_included) VALUES
-- Gym Basic Monthly
(1, 1, 1), -- 1 month gym membership

-- Gym Premium 3 Months
(2, 2, 1), -- 3 months gym membership
(2, 3, 4), -- 4 personal training sessions

-- Gym VIP 6 Months
(3, 2, 2), -- 2 x 3 months gym membership
(3, 3, 12), -- 12 personal training sessions

-- Bowling Starter Pack
(4, 4, 10), -- 10 hourly bowling sessions

-- Bowling Pro Pack
(5, 4, 20), -- 20 hourly bowling sessions

-- Bowling Unlimited Monthly
(6, 4, 999), -- Unlimited (represented as 999)

-- Fitness + Bowling Combo
(7, 7, 1), -- Gym + Bowling combo service
(7, 4, 5), -- 5 bowling sessions

-- Premium Sports Package
(8, 7, 3), -- 3 months of gym + bowling combo
(8, 8, 12); -- 12 fitness class + bowling sessions

-- Insert sample admin user
INSERT INTO users (firebase_uid, email, full_name, role_id) VALUES
('admin_firebase_uid', 'admin@gymbowling.com', 'System Administrator', 1);

-- =====================================================
-- CREATE VIEWS FOR COMMON QUERIES
-- =====================================================

-- View for active package plans with center info
GO
CREATE VIEW v_active_package_plans AS
SELECT 
    pp.id,
    pp.name,
    pp.description,
    pp.price,
    pp.duration_months,
    pp.is_active,
    pp.created_at,
    c.id as center_id,
    c.name as center_name,
    c.address as center_address
FROM package_plans pp
JOIN centers c ON pp.center_id = c.id
WHERE pp.is_active = 1;
GO

-- View for services with center and service type info
GO
CREATE VIEW v_services_with_details AS
SELECT 
    s.id,
    s.name,
    s.description,
    s.price,
    s.is_active,
    s.created_at,
    c.id as center_id,
    c.name as center_name,
    st.id as service_type_id,
    st.name as service_type_name
FROM services s
JOIN centers c ON s.center_id = c.id
JOIN service_types st ON s.service_type_id = st.id
WHERE s.is_active = 1;
GO

-- View for time frames with center info
GO
CREATE VIEW v_timeframes_with_center AS
SELECT 
    tf.id,
    tf.day_of_week,
    tf.start_time,
    tf.end_time,
    tf.is_available,
    tf.created_at,
    c.id as center_id,
    c.name as center_name
FROM time_frames tf
JOIN centers c ON tf.center_id = c.id
WHERE tf.is_available = 1;
GO

-- =====================================================
-- CREATE STORED PROCEDURES
-- =====================================================

-- Procedure to get package plans by center
GO
CREATE PROCEDURE sp_GetPackagePlansByCenter
    @CenterId BIGINT
AS
BEGIN
    SELECT 
        pp.id,
        pp.name,
        pp.description,
        pp.price,
        pp.duration_months,
        pp.is_active,
        pp.created_at,
        c.name as center_name
    FROM package_plans pp
    JOIN centers c ON pp.center_id = c.id
    WHERE pp.center_id = @CenterId AND pp.is_active = 1
    ORDER BY pp.price ASC;
END
GO

-- Procedure to get services by center and service type
GO
CREATE PROCEDURE sp_GetServicesByCenterAndType
    @CenterId BIGINT,
    @ServiceTypeId BIGINT
AS
BEGIN
    SELECT 
        s.id,
        s.name,
        s.description,
        s.price,
        s.is_active,
        s.created_at,
        c.name as center_name,
        st.name as service_type_name
    FROM services s
    JOIN centers c ON s.center_id = c.id
    JOIN service_types st ON s.service_type_id = st.id
    WHERE s.center_id = @CenterId 
        AND s.service_type_id = @ServiceTypeId 
        AND s.is_active = 1
    ORDER BY s.price ASC;
END
GO

-- Procedure to get time frames by center and day
GO
CREATE PROCEDURE sp_GetTimeFramesByCenterAndDay
    @CenterId BIGINT,
    @DayOfWeek NVARCHAR(20)
AS
BEGIN
    SELECT 
        tf.id,
        tf.day_of_week,
        tf.start_time,
        tf.end_time,
        tf.is_available,
        c.name as center_name
    FROM time_frames tf
    JOIN centers c ON tf.center_id = c.id
    WHERE tf.center_id = @CenterId 
        AND tf.day_of_week = @DayOfWeek 
        AND tf.is_available = 1
    ORDER BY tf.start_time ASC;
END
GO

-- =====================================================
-- FINAL VERIFICATION
-- =====================================================

-- Verify all tables were created
SELECT 
    TABLE_NAME,
    TABLE_TYPE
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;

-- Verify all foreign key relationships
SELECT 
    fk.name as constraint_name,
    OBJECT_NAME(fk.parent_object_id) as table_name,
    COL_NAME(fkc.parent_object_id, fkc.parent_column_id) as column_name,
    OBJECT_NAME(fk.referenced_object_id) as referenced_table_name,
    COL_NAME(fkc.referenced_object_id, fkc.referenced_column_id) as referenced_column_name
FROM sys.foreign_keys fk
INNER JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
ORDER BY table_name, column_name;

-- Count records in each table
SELECT 'roles' as table_name, COUNT(*) as record_count FROM roles
UNION ALL
SELECT 'users', COUNT(*) FROM users
UNION ALL
SELECT 'centers', COUNT(*) FROM centers
UNION ALL
SELECT 'service_types', COUNT(*) FROM service_types
UNION ALL
SELECT 'services', COUNT(*) FROM services
UNION ALL
SELECT 'time_frames', COUNT(*) FROM time_frames
UNION ALL
SELECT 'package_plans', COUNT(*) FROM package_plans
UNION ALL
SELECT 'package_plan_details', COUNT(*) FROM package_plan_details;

PRINT 'Database setup completed successfully!';
PRINT 'Gym Bowling Backend is ready to use.';
GO
