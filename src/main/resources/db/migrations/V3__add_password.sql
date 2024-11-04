ALTER TABLE users
ADD COLUMN password VARCHAR(128) NOT NULL,
DROP COLUMN address,
DROP COLUMN bank_account,
DROP COLUMN director_name,
DROP COLUMN form_of_ownership,
DROP COLUMN manager_name;