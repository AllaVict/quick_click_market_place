INSERT INTO users
(first_name, last_name, password, email, email_confirmed,
 phone, phone_displayed, role, status,
 sex, locale, created_date, updated_date)
VALUES ('Admin', 'Adminuch', '{noop}123', 'admin@gmail.com', true,
        '+380507778855', false, 'ROLE_ADMIN', 'ACTIVE',
        'FEMALE', 'UK', now(), now()),
       ('UserActive', 'Useruch', '{noop}123', 'userActive@gmail.com', true,
        '+380503338855', true, 'ROLE_USER', 'ACTIVE',
        'MALE', 'EN', now(), now()),
       ('UserBlocked', 'Useruch', '{noop}123', 'UserBlocked@gmail.com', true,
        '+380504448855', false, 'ROLE_USER', 'BLOCKED',
        'FEMALE', 'UK', now(), now()),
       ('UserDeleted', 'Useruch', '{noop}123', 'UserDeleted@gmail.com', true,
        '+380502228855', false, 'ROLE_USER', 'DELETED',
        'MALE', 'EN', now(), now());



