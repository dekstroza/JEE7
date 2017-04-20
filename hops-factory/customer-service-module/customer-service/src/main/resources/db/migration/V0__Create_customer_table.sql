CREATE USER customer_service_app WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD '123hopS';

CREATE SCHEMA customer_service
    AUTHORIZATION customer_service_app;
CREATE TABLE customer_service.customers
(
  id uuid NOT NULL DEFAULT public.gen_random_uuid(),
  firstname character varying,
  lastname character varying,
  email character varying,
  password character varying,
  CONSTRAINT pk_id PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE customer_service.customers
  OWNER TO customer_service_app;