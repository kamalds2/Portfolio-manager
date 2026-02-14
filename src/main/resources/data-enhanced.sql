-- Enhanced initialization with sample portfolio data
-- This runs automatically when the application starts

-- Initialize admin user if not exists  
INSERT IGNORE INTO users (username, password, email, created_at, updated_at) 
VALUES ('admin', '$2a$10$dXJ3SW6G7P50.2dktmVOeOe1TaHQ.LxvZTyMEyKV2XGQVmL3K11K.', 'admin@managefolio.com', NOW(), NOW());

INSERT IGNORE INTO user_roles (user_id, role) 
SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin';

-- Sample Profile Data (optional - remove if you want to add manually)
INSERT IGNORE INTO profiles (user_id, full_name, title, bio, email, phone, location, created_at, updated_at)
SELECT u.id, 'Portfolio Administrator', 'System Administrator', 
       'Managing portfolio system and user accounts.', 
       'admin@managefolio.com', '+1-234-567-8900', 'Cloud Platform',
       NOW(), NOW()
FROM users u WHERE u.username = 'admin';

-- Sample Skills (optional)
INSERT IGNORE INTO skills (profile_id, name, category, proficiency_level, created_at, updated_at)
SELECT p.id, 'Spring Boot', 'Backend', 'Expert', NOW(), NOW()
FROM profiles p 
JOIN users u ON p.user_id = u.id 
WHERE u.username = 'admin';

INSERT IGNORE INTO skills (profile_id, name, category, proficiency_level, created_at, updated_at)
SELECT p.id, 'Google Cloud', 'Cloud Platform', 'Advanced', NOW(), NOW()
FROM profiles p 
JOIN users u ON p.user_id = u.id 
WHERE u.username = 'admin';