CREATE TABLE IF NOT EXISTS images(
   id   BIGINT AUTO_INCREMENT,
   name  VARCHAR(100),
   type  VARCHAR(50),
   image_data BLOB(4000K),
   user_id   BIGINT,
   advert_id   BIGINT,
   created_date TIMESTAMP,
   updated_date TIMESTAMP,
   PRIMARY KEY (id)
);

ALTER TABLE images ADD CONSTRAINT advertimages_fk FOREIGN KEY (advert_id) REFERENCES adverts(id);
ALTER TABLE adverts ALTER COLUMN address VARCHAR(400);

