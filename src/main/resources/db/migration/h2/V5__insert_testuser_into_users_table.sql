ALTER TABLE users ALTER COLUMN password DROP NOT NULL;
INSERT INTO users
(first_name, last_name, email, email_confirmed,
 phone, phone_displayed, role, status,
 sex, locale, created_date, updated_date)
VALUES
       ('UserGoogle', 'Useruch', 'allavict75@gmail.com', true,
        '+380503338855', true, 'ROLE_USER', 'ACTIVE',
        'MALE', 'EN', now(), now());


