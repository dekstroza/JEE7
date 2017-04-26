CREATE SCHEMA inventory_service
    AUTHORIZATION inventory_service_app;

CREATE TABLE inventory_service.inventory
(
  id uuid NOT NULL DEFAULT public.gen_random_uuid(),
  supplierId uuid,
  name VARCHAR,
  description VARCHAR,
  price REAL,
  availableQuantity REAL,
  CONSTRAINT pk_id PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE inventory_service.inventory
  OWNER TO inventory_service_app;