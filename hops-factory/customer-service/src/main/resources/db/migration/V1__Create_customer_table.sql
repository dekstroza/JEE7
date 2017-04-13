CREATE TABLE customers
(
  id uuid NOT NULL DEFAULT gen_random_uuid(),
  firstname character varying,
  lastname character varying,
  email character varying,
  password character varying,
  CONSTRAINT pk_id PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE customers
  OWNER TO customer_service_app;