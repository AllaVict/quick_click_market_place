ALTER TABLE adverts ADD viewing_quantity INT;

UPDATE adverts SET viewing_quantity = 0;