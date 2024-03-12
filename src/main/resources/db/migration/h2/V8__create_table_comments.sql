CREATE TABLE IF NOT EXISTS comments(
   id  BIGINT AUTO_INCREMENT,
   message  VARCHAR(100),
   advert_id BIGINT,
   username  VARCHAR(50),
   user_id  BIGINT,
   created_date TIMESTAMP,
   updated_date TIMESTAMP,
   PRIMARY KEY (id)
);

ALTER TABLE comments ADD CONSTRAINT usercomments_fk FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE comments ADD CONSTRAINT advertcomments_fk FOREIGN KEY (advert_id) REFERENCES adverts(id);

INSERT INTO comments
(message, advert_id, username, user_id, created_date)
VALUES
    ('perfect!! Do you propose some discount?', 1, 'Admin', 1, now()),
    ('excellent!!', 2, 'UserActive', 1, now()),
    ('Serh gut... ', 1, 'Admin', 1, now()),
    ('exzellent))) ', 2, 'UserActive', 1, now()),
    ('so nice)) do you have a white one? ', 2, 'Admin', 1, now()),
    ('yes, we have white, black and grey', 2, 'UserActive', 2, now()),
    ('das ist exzellent)))', 1, 'Admin', 2, now());

