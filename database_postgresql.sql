-- =====================================================
-- GYM BOWLING BACKEND DATABASE SCRIPT
-- PostgreSQL Version for Render
-- =====================================================

-- Create database (Render sẽ tạo sẵn)
-- CREATE DATABASE gym_bowling;

-- Connect to database
-- \c gym_bowling;

-- =====================================================
-- CREATE TABLES
-- =====================================================

-- 1. ROLES TABLE
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    firebase_uid VARCHAR(255) UNIQUE,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 3. CENTERS TABLE
CREATE TABLE IF NOT EXISTS centers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(500),
    phone VARCHAR(20),
    email VARCHAR(255),
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- 4. SERVICE_TYPES TABLE
CREATE TABLE IF NOT EXISTS service_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. SERVICES TABLE
CREATE TABLE IF NOT EXISTS services (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    center_id BIGINT NOT NULL,
    service_type_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (center_id) REFERENCES centers(id),
    FOREIGN KEY (service_type_id) REFERENCES service_types(id)
);

-- 6. TIME_FRAMES TABLE
CREATE TABLE IF NOT EXISTS time_frames (
    id BIGSERIAL PRIMARY KEY,
    center_id BIGINT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_available BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (center_id) REFERENCES centers(id)
);

-- 7. PACKAGE_PLANS TABLE
CREATE TABLE IF NOT EXISTS package_plans (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    duration_months INTEGER NOT NULL,
    center_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (center_id) REFERENCES centers(id)
);

-- 8. PACKAGE_PLAN_DETAILS TABLE
CREATE TABLE IF NOT EXISTS package_plan_details (
    id BIGSERIAL PRIMARY KEY,
    package_plan_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    sessions_included INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (package_plan_id) REFERENCES package_plans(id),
    FOREIGN KEY (service_id) REFERENCES services(id)
);

-- 9. ORDERS TABLE
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 10. ORDER_PACKAGES TABLE
CREATE TABLE IF NOT EXISTS order_packages (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    package_plan_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (package_plan_id) REFERENCES package_plans(id)
);

-- 11. PAYMENTS TABLE
CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) DEFAULT 'VNPAY',
    status VARCHAR(50) DEFAULT 'PENDING',
    transaction_id VARCHAR(255),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    vnpay_response TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- =====================================================
-- CREATE INDEXES FOR PERFORMANCE
-- =====================================================

-- Users indexes
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_firebase_uid ON users(firebase_uid);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role_id ON users(role_id);

-- Services indexes
CREATE INDEX IF NOT EXISTS idx_services_center_id ON services(center_id);
CREATE INDEX IF NOT EXISTS idx_services_service_type_id ON services(service_type_id);
CREATE INDEX IF NOT EXISTS idx_services_active ON services(is_active);

-- Time frames indexes
CREATE INDEX IF NOT EXISTS idx_timeframes_center_id ON time_frames(center_id);
CREATE INDEX IF NOT EXISTS idx_timeframes_day_available ON time_frames(day_of_week, is_available);

-- Package plans indexes
CREATE INDEX IF NOT EXISTS idx_package_plans_center_id ON package_plans(center_id);
CREATE INDEX IF NOT EXISTS idx_package_plans_active ON package_plans(is_active);

-- Orders indexes
CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_date ON orders(order_date);

-- Order packages indexes
CREATE INDEX IF NOT EXISTS idx_order_packages_order_id ON order_packages(order_id);
CREATE INDEX IF NOT EXISTS idx_order_packages_package_plan_id ON order_packages(package_plan_id);

-- Payments indexes
CREATE INDEX IF NOT EXISTS idx_payments_order_id ON payments(order_id);
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);
CREATE INDEX IF NOT EXISTS idx_payments_transaction_id ON payments(transaction_id);

-- =====================================================
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert default roles
INSERT INTO roles (name, description) VALUES
('ADMIN', 'System administrator with full access'),
('STAFF', 'Staff member with center management access'),
('USER', 'Regular user with basic access')
ON CONFLICT (name) DO NOTHING;

-- Insert sample centers
INSERT INTO centers (name, address, phone, email, description) VALUES
('Center A', '123 Main Street, District 1, HCMC', '0901234567', 'centera@example.com', 'Premium gym and fitness center with modern equipment and professional trainers'),
('Center B', '456 Business Avenue, District 3, HCMC', '0901234568', 'centerb@example.com', 'Professional bowling center with multiple lanes and tournament facilities'),
('Center C', '789 Central Road, District 1, HCMC', '0901234569', 'centerc@example.com', 'Combined sports complex offering both gym and bowling facilities')
ON CONFLICT (name) DO NOTHING;

-- Insert service types
INSERT INTO service_types (name, description) VALUES
('GYM', 'Gym and fitness services'),
('BOWLING', 'Bowling and entertainment services')
ON CONFLICT (name) DO NOTHING;

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
('Fitness Class + Bowling', 'Fitness class followed by bowling', 400000.00, 3, 1)
ON CONFLICT DO NOTHING;

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
(3, 'TUESDAY', '06:00:00', '23:00:00'),
(3, 'FRIDAY', '06:00:00', '00:00:00'),
(3, 'SATURDAY', '07:00:00', '00:00:00'),
(3, 'SUNDAY', '07:00:00', '23:00:00')
ON CONFLICT DO NOTHING;

-- Insert sample package plans
INSERT INTO package_plans (name, description, price, duration_months, center_id) VALUES
-- Center A (Gym Focus) packages
('Gym Basic Monthly', 'Basic gym access for 1 month', 500000.00, 1, 1),
('Gym Premium 3 Months', 'Premium gym access for 3 months with personal training', 2000000.00, 3, 1),
('Gym VIP 6 Months', 'VIP gym access for 6 months with all amenities', 3500000.00, 6, 1),

-- Center B (Bowling Focus) packages
('Bowling Basic Monthly', 'Basic bowling access for 1 month', 300000.00, 1, 2),
('Bowling Premium 3 Months', 'Premium bowling access for 3 months', 800000.00, 3, 2),
('Bowling VIP 6 Months', 'VIP bowling access for 6 months', 1500000.00, 6, 2),

-- Center C (Combined) packages
('Combo Basic Monthly', 'Basic gym + bowling access for 1 month', 600000.00, 1, 3),
('Combo Premium 3 Months', 'Premium gym + bowling access for 3 months', 1600000.00, 3, 3),
('Combo VIP 6 Months', 'VIP gym + bowling access for 6 months', 3000000.00, 6, 3)
ON CONFLICT DO NOTHING;

-- Insert package plan details
INSERT INTO package_plan_details (package_plan_id, service_id, sessions_included) VALUES
-- Gym packages
(1, 1, 1), -- Basic Monthly
(2, 2, 3), -- Premium 3 Months
(3, 3, 6), -- VIP 6 Months

-- Bowling packages
(4, 4, 1), -- Basic Monthly
(5, 5, 3), -- Premium 3 Months
(6, 6, 6), -- VIP 6 Months

-- Combo packages
(7, 7, 1), -- Basic Monthly
(8, 8, 3), -- Premium 3 Months
(9, 9, 6)  -- VIP 6 Months
ON CONFLICT DO NOTHING;

-- =====================================================
-- FINAL VERIFICATION
-- =====================================================

-- Verify all tables were created
SELECT 
    table_name,
    table_type
FROM information_schema.tables 
WHERE table_schema = 'public'
ORDER BY table_name;

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

-- Print completion message
DO $$
BEGIN
    RAISE NOTICE 'Database setup completed successfully!';
    RAISE NOTICE 'Gym Bowling Backend is ready to use on Render.';
END $$;
