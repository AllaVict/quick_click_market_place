ALTER TABLE adverts ADD viewer_id BIGINT;

ALTER TABLE adverts ADD CONSTRAINT viewers_fk FOREIGN KEY (viewer_id) REFERENCES users(id);

UPDATE adverts SET viewer_id = 0;
