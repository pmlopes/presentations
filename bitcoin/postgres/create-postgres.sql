BEGIN;

CREATE TABLE UTX (
  id SERIAL PRIMARY KEY,
  data json
);

GRANT SELECT, INSERT ON UTX to dbuser;
GRANT USAGE, SELECT ON SEQUENCE utx_id_seq TO dbuser;

COMMIT;