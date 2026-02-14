-- Enhanced initialization for Cloud SQL (MySQL)
-- This runs automatically when the application starts

-- Initialize admin user if not exists  
-- Password: 'admin123' (BCrypt encoded)
INSERT INTO users (username, password, email, created_at, updated_at) 
VALUES ('admin', '$2a$10$dXJ3SW6G7P50.2dktmVOeOe1TaHQ.LxvZTyMEyKV2XGQVmL3K11K.', 'admin@managefolio.com', NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email), updated_at = NOW();

-- Add admin role
INSERT INTO user_roles (user_id, role) 
SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    WHERE ur.user_id = users.id AND ur.role = 'ROLE_ADMIN'
);