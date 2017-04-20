CREATE USER order_service_app WITH
	LOGIN
	NOSUPERUSER
	NOCREATEDB
	NOCREATEROLE
	INHERIT
	NOREPLICATION
	CONNECTION LIMIT -1
	PASSWORD '123hopS';

CREATE SCHEMA order_service
    AUTHORIZATION order_service_app;

CREATE TABLE order_service.orders
(
  id UUID NOT NULL DEFAULT public.gen_random_uuid(),
  inventoryId UUID,
  customerId UUID,
  quantity REAL,
  price REAL,
  orderDate TIMESTAMP DEFAULT now(),
  status VARCHAR,
  CONSTRAINT pk_id PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE order_service.orders
  OWNER TO order_service_app;