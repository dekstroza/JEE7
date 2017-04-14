ALTER TABLE customers
   ALTER COLUMN email TYPE text;
ALTER TABLE customers
  ADD CONSTRAINT uniq_email UNIQUE (email);
CREATE INDEX idx_password
   ON customers (password ASC NULLS LAST);