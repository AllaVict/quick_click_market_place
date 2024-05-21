ALTER TABLE adverts ADD viewer_id BIGINT;

UPDATE adverts SET viewer_id = 0;
