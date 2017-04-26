CREATE SCHEMA supplier_service
    AUTHORIZATION supplier_service_app;

CREATE TABLE supplier_service.suppliers
(
  id UUID NOT NULL DEFAULT public.gen_random_uuid(),
  name VARCHAR,
  address VARCHAR,
  phone VARCHAR,
  email VARCHAR,
  CONSTRAINT pk_id PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE supplier_service.suppliers
  OWNER TO supplier_service_app;