DROP TABLE items;

CREATE TABLE items (
   id SERIAL PRIMARY KEY,
   name VARCHAR(50) NOT NULL,
   quantity INT NOT NULL,
   price NUMERIC(6,2) NOT NULL,
   description VARCHAR(255)
);

INSERT INTO items (name,quantity,price,description) VALUES ('Chair',5,15.00,'A ordinary chair');
INSERT INTO items (name,quantity,price,description) VALUES ('Table',1,102.00,'A ordinary table');
INSERT INTO items (name,quantity,price,description) VALUES ('Lamp',10,50.00,'A ordinary lamp');

