INSERT INTO adverts
(title, description, category, status,
 phone, price, first_price, first_price_displayed, currency,
 address, favorite, image_id, user_id, created_date)
VALUES
    ('Red Volvo', 'description a auto Volvo', 'AUTO',  'PUBLISHED',
     '+380503332244',100000.00, 70000.00, true, 'EUR',
     'Berlin', true,1001, 1, now()),
    ('Blue jeans', 'description a clothes Blue jeans', 'CLOTHES',  'PUBLISHED',
     '+380507778855',20.00, 10.00, true, 'USD',
     'Dania', false,1001, 1, now()),
    ('Black cat', 'description a toy Black Cat', 'TOYS',  'PUBLISHED',
     '+380507778855',100.00, 80.00, true, 'UAH',
     'Lviv', false, 1001, 1, now());