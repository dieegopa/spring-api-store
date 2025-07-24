INSERT INTO categories (name)
VALUES ('Electronics'),
       ('Clothing'),
       ('Home'),
       ('Books'),
       ('Sports');

INSERT INTO products (name, price, description, category_id)
VALUES ('Bluetooth Headphones', 49.99, 'Wireless headphones with noise cancellation.', 1),
       ('Basic T-Shirt', 9.99, '100% cotton T-shirt available in multiple colors.', 2),
       ('LED Lamp', 19.99, 'Desk lamp with adjustable LED lighting.', 3),
       ('Mystery Novel', 14.50, 'Suspense book from a renowned author.', 4),
       ('Soccer Ball', 25.00, 'Official size 5 soccer ball.', 5),
       ('24-Inch Monitor', 139.90, 'Full HD LED monitor with HDMI and VGA inputs.', 1),
       ('Sports Pants', 29.99, 'Comfortable pants for working out.', 2),
       ('Non-stick Frying Pan', 22.95, '28cm frying pan ideal for oil-free cooking.', 3),
       ('School Encyclopedia', 59.99, 'Comprehensive encyclopedia for students.', 4),
       ('Tennis Racket', 89.00, 'Professional racket with included cover.', 5);
