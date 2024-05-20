INSERT INTO users
(first_name, last_name, password, email, email_confirmed,
 phone, phone_displayed, role, status,
 sex, locale, created_date, updated_date)
VALUES ('Admin', 'Adminuch', '$2a$10$tJr0LrFex.6Ri8Q1m/pvDO7ezNu7uAKWzHZQJ9gpUk/cXIPY5bDj6', 'admin@gmail.com', true,
        '+380507778855', false, 'ROLE_ADMIN', 'ACTIVE',
        'FEMALE', 'UK', now(), now()),
       ('UserActive', 'Useruch', '$2a$10$tJr0LrFex.6Ri8Q1m/pvDO7ezNu7uAKWzHZQJ9gpUk/cXIPY5bDj6', 'userActive@gmail.com', true,
        '+380503338855', true, 'ROLE_USER', 'ACTIVE',
        'MALE', 'EN', now(), now()),
       ('UserBlocked', 'Useruch', '$2a$10$tJr0LrFex.6Ri8Q1m/pvDO7ezNu7uAKWzHZQJ9gpUk/cXIPY5bDj6', 'UserBlocked@gmail.com', true,
        '+380504448855', false, 'ROLE_USER', 'BLOCKED',
        'FEMALE', 'UK', now(), now()),
       ('UserDeleted', 'Useruch', '$2a$10$tJr0LrFex.6Ri8Q1m/pvDO7ezNu7uAKWzHZQJ9gpUk/cXIPY5bDj6', 'UserDeleted@gmail.com', true,
        '+380502228855', false, 'ROLE_USER', 'DELETED',
        'MALE', 'EN', now(), now());



