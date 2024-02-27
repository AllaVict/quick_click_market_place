CREATE TABLE adverts (
    id BIGINT NOT NULL  AUTO_INCREMENT ,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    category VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    phone VARCHAR(50),
    price FLOAT NOT NULL,
    first_price FLOAT,
    first_price_displayed BOOLEAN,
    currency VARCHAR(50),
    address VARCHAR(100) NOT NULL,
    image_id BIGINT,
    user_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP,
    PRIMARY KEY (id)
);

ALTER TABLE adverts ADD CONSTRAINT users_fk FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE adverts ADD CONSTRAINT file_ref_ad FOREIGN KEY (image_id) REFERENCES file_references (id);
