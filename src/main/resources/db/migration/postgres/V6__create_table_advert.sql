CREATE TABLE adverts (
    id BIGSERIAL NOT NULL ,
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
    favorite BOOLEAN,
    image_id BIGINT,
    user_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP,
    PRIMARY KEY (id)
);

ALTER TABLE adverts ADD CONSTRAINT users_fk FOREIGN KEY (user_id) REFERENCES users(id);



