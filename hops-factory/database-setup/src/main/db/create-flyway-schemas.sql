CREATE SCHEMA ${db.customer.service.flyway.schema} AUTHORIZATION ${db.username.dev};
CREATE SCHEMA ${db.customer.service.flyway.schema} AUTHORIZATION ${db.username.dev};
CREATE SCHEMA ${db.inventory.service.flyway.schema} AUTHORIZATION ${db.username.dev};
CREATE SCHEMA ${db.order.service.flyway.schema} AUTHORIZATION ${db.username.dev};
CREATE SCHEMA ${db.supplier.service.flyway.schema} AUTHORIZATION ${db.username.dev};
CREATE EXTENSION IF NOT EXISTS pgcrypto;
