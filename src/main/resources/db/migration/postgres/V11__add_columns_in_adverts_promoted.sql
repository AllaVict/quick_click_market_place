ALTER TABLE adverts ADD promoted BOOLEAN;

UPDATE adverts SET promoted = false;