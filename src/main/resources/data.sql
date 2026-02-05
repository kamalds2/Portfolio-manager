-- Initialize admin user if not exists
INSERT IGNORE INTO users (username, password, email, created_at, updated_at) 
VALUES ('admin', '$2a$10$35.M3KZlvhETq7VbG/vRN.RPkA8FLAQ23cWeQKfp.CCu3xLBnnsri', 'admin@managefolio.com', NOW(), NOW());

INSERT IGNORE INTO user_roles (user_id, role) 
SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin';