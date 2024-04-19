CREATE TABLE IF NOT EXISTS images(
   id   BIGSERIAL NOT NULL ,
   name  VARCHAR(100),
   type  VARCHAR(50),
   image_data BYTEA,
   user_id   BIGINT,
   advert_id   BIGINT,
   created_date TIMESTAMP,
   updated_date TIMESTAMP,
   PRIMARY KEY (id)
);

ALTER TABLE images ADD CONSTRAINT advertimages_fk FOREIGN KEY (advert_id) REFERENCES adverts(id);
ALTER TABLE adverts ALTER COLUMN address VARCHAR(400);
