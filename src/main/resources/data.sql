-- Initialize admin user if not exists
-- Password: 'admin123' (BCrypt encoded)
INSERT IGNORE INTO users (username, password, email, created_at, updated_at) 
VALUES ('admin', '$2a$10$dXJ3SW6G7P50.2dktmVOeOe1TaHQ.LxvZTyMEyKV2XGQVmL3K11K.', 'admin@managefolio.com', NOW(), NOW());

INSERT IGNORE INTO user_roles (user_id, role) 
SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin';