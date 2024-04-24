CREATE TABLE IF NOT EXISTS images
(
    id           BIGINT AUTO_INCREMENT,
    name         VARCHAR(100),
    type         VARCHAR(50),
    image_data   BLOB,
    user_id      BIGINT,
    advert_id    BIGINT,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    PRIMARY KEY (id)
);

ALTER TABLE images
    ADD CONSTRAINT advertimages_fk FOREIGN KEY (advert_id) REFERENCES adverts (id);
ALTER TABLE adverts
    ALTER COLUMN address VARCHAR(400);

INSERT INTO images (id, name, type, user_id, advert_id, created_date)
VALUES (1, 'c63b8ca2-f359-4dd2-8c37-0ffa8ae74048.jpg', 'image/jpeg', 1, 1, NOW()),
       (2, 'c112c47e-b1d3-4cbc-acb5-a72767dbe0d4.jpg', 'image/jpeg', 1, 2, NOW()),
       (3, '1d058f60-d40a-4082-9b99-5d3308d26f33.jpg', 'image/jpeg', 1, 3, NOW()),
       (4, 'fb830c66-2044-4976-bf83-868a04f907de.jpg', 'image/jpeg', 1, 4, NOW()),
       (5, '2ce96b38-eac4-43e9-8675-1f921b09433b.jpg', 'image/jpeg', 1, 5, NOW()),
       (6, '1768092b-9388-40a5-8e78-ad8c4d8e1a5c.jpg', 'image/jpeg', 1, 6, NOW());
