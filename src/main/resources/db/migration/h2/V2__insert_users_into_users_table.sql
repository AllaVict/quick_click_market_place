INSERT INTO users
(first_name, last_name, password, email, email_confirmed,
 phone, phone_displayed, role, status,
 sex, locale, created_date, updated_date)
VALUES ('Admin', 'Adminuch', '$2a$10$IWd1mpWJfPFLou/4FZZarehk1hVSSj6cxOkK4n7haaNdQ8oe/BIJm', 'admin@gmail.com', true,
        '+380507778855', false, 'ROLE_ADMIN', 'ACTIVE',
        'FEMALE', 'UK', now(), now()),
       ('UserActive', 'Useruch', '$2a$10$IWd1mpWJfPFLou/4FZZarehk1hVSSj6cxOkK4n7haaNdQ8oe/BIJm', 'userActive@gmail.com', true,
        '+380503338855', true, 'ROLE_USER', 'ACTIVE',
        'MALE', 'EN', now(), now()),
       ('UserBlocked', 'Useruch', '$2a$10$IWd1mpWJfPFLou/4FZZarehk1hVSSj6cxOkK4n7haaNdQ8oe/BIJm', 'UserBlocked@gmail.com', true,
        '+380504448855', false, 'ROLE_USER', 'BLOCKED',
        'FEMALE', 'UK', now(), now()),
       ('UserDeleted', 'Useruch', '$2a$10$IWd1mpWJfPFLou/4FZZarehk1hVSSj6cxOkK4n7haaNdQ8oe/BIJm', 'UserDeleted@gmail.com', true,
        '+380502228855', false, 'ROLE_USER', 'DELETED',
        'MALE', 'EN', now(), now());



