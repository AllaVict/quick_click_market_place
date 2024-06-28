ALTER TABLE adverts DROP CONSTRAINT viewers_fk;
ALTER TABLE adverts DROP COLUMN viewer_id;

CREATE TABLE advert_viewers (
    advert_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (advert_id, user_id),
    FOREIGN KEY (advert_id) REFERENCES adverts(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

