
ROLLBACK;
DROP TRIGGER IF EXISTS delete_company_trigger ON insurance_companies;
DROP TRIGGER IF EXISTS salary_not_negative_trigger ON users;
DROP TRIGGER IF EXISTS delete_user_trigger ON users;
DROP TRIGGER IF EXISTS salary_limit_not_negative_trigger ON services;
DROP TRIGGER IF EXISTS salary_limit_check_trigger ON services;
DROP FUNCTION IF EXISTS delete_company();
DROP FUNCTION IF EXISTS delete_user();
DROP FUNCTION IF EXISTS salary_not_negative();
DROP FUNCTION IF EXISTS salary_limit_not_negative();
DROP FUNCTION IF EXISTS salary_limit_check();

DROP TABLE IF EXISTS cars_insurance_bounds;
DROP TABLE IF EXISTS users_cars;
DROP TABLE IF EXISTS users_services;
DROP TABLE IF EXISTS insurance_bounds;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS insurance_companies;



CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    salary INTEGER,
    CONSTRAINT email_not_null CHECK (email <> ''),
    CONSTRAINT salary_not_negative CHECK (salary > 0)
);



CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    licence_plate TEXT UNIQUE NOT NULL,
    manufacturer TEXT NOT NULL,
    model_name TEXT NOT NULL,
    CONSTRAINT licence_plate_not_null CHECK (licence_plate <> ''),
    CONSTRAINT manufacturer_not_null CHECK (manufacturer <> ''),
    CONSTRAINT model_name_not_null CHECK (model_name <> '')
);


CREATE TABLE users_cars (
    user_id INTEGER NOT NULL,
    car_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE insurance_companies (
    id SERIAL PRIMARY KEY,
    company_name TEXT UNIQUE NOT NULL,
    CONSTRAINT company_name_not_null CHECK (company_name <> '')
);

CREATE TABLE services (
    id SERIAL PRIMARY KEY,
    service_name TEXT NOT NULL,
    salary_limit INTEGER,
    valid_years INTEGER,
    issuer INTEGER,
    CONSTRAINT service_name_not_null CHECK (service_name <> ''),
    CONSTRAINT salary_limit_not_negative CHECK (salary_limit > 0),
    CONSTRAINT valid_years_more_then_zero CHECK (valid_years > 0),
    FOREIGN KEY (issuer) REFERENCES insurance_companies(id)

);

CREATE TABLE users_services (
    user_id INTEGER NOT NULL,
    services_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (services_id) REFERENCES services(id)
);

CREATE TABLE insurance_bounds ( 
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    car_id INT NOT NULL,
    issued_date DATE NOT NULL,
    CONSTRAINT issued_date_not_null CHECK (issued_date IS NOT NULL),
    FOREIGN KEY (car_id) REFERENCES cars(id),
    FOREIGN KEY (user_id) REFERENCES users(id)

);

CREATE TABLE cars_insurance_bounds(
    car_id INTEGER NOT NULL,
    insurance_bound_id INTEGER NOT NULL,    
    FOREIGN KEY (insurance_bound_id) REFERENCES insurance_bounds(id),
    FOREIGN KEY (car_id) REFERENCES cars(id)    
);

CREATE FUNCTION salary_limit_check() RETURNS trigger AS $$
DECLARE 
customer_salary INTEGER;
service_to_user INTEGER;
salary_lim INTEGER;
BEGIN
    SELECT salary_lim INTO salary_lim FROM services WHERE id=NEW.id;
    SELECT user_id INTO service_to_user FROM users_services WHERE services_id = NEW.id;
	SELECT salary INTO customer_salary FROM users WHERE id = service_to_user;
    IF NEW.salary_limit > customer_salary THEN 
    RAISE EXCEPTION '% is insufficient founds, cannot join buy this insurance: %', customer_salary, service_to_user;
    END IF;	
    RETURN NEW;
END$$ language plpgsql;

CREATE TRIGGER salary_limit_check_trigger BEFORE UPDATE OR INSERT ON services FOR EACH ROW EXECUTE PROCEDURE salary_limit_check();



CREATE FUNCTION delete_user() RETURNS trigger AS $del_usr$
BEGIN
	DELETE FROM insurance_bounds WHERE insurance_bounds.user_id = OLD.id;
RETURN OLD;
END$del_usr$ LANGUAGE plpgsql;

CREATE TRIGGER delete_user_trigger BEFORE DELETE ON users FOR EACH ROW EXECUTE PROCEDURE delete_user();




CREATE FUNCTION delete_company() RETURNS trigger AS $del_cmp$
BEGIN
    IF EXISTS (SELECT insurance_companies.id FROM insurance_companies JOIN services ON services.id=issuer 
    JOIN users_services ON users_services.services_id = services.id
    JOIN users ON users.id = users_services.user_id
    JOIN insurance_bounds ON insurance_bounds.user_id = users.id
    WHERE insurance_companies.id = OLD.id)
    THEN RAISE EXCEPTION 'there is an insurance bounded to this company';
	END IF;
RETURN OLD;
END$del_cmp$ LANGUAGE plpgsql;

CREATE TRIGGER delete_company_trigger BEFORE DELETE ON insurance_companies FOR EACH ROW EXECUTE PROCEDURE delete_company();



INSERT INTO users(email) VALUES  -- add users (without salary)
    ('eviltwin@gmail.com'),
    ('weedeater@gmail.com');


INSERT INTO users(email, salary) VALUES  -- add users
    ('CottonEyeJoeJoe@farm.com', 2500),
	('shepardpie@snake.com', 45000),
	('richbaster@snake.com', 100000),
    ('redneck99@farm.com', 20),
	('snakemilker6000@snake.com', 300);

SELECT users.email FROM users ORDER BY id ASC;  --show users but only email

SELECT * FROM users ORDER BY id ASC;  --show users with all data

SELECT * FROM users WHERE id = 1;  --show a specific user's all data

SELECT * FROM users WHERE salary IS NULL;  -- show users without salary

BEGIN;  -- delete user (needed to add extra data)
INSERT INTO cars(licence_plate, manufacturer, model_name) VALUES
    ('HAC-879', 'Ford', '2000gt');
INSERT INTO insurance_bounds(user_id, car_id, issued_date) SELECT 2, MAX(id),'2000-11-05' FROM cars;
DELETE FROM users WHERE id = 2;
COMMIT;

BEGIN;  -- add new cars
INSERT INTO cars(licence_plate, manufacturer, model_name) VALUES
    ('BLD-233', 'Lada', '1000'),
    ('JKL-110', 'Lotus', 'exige'),
    ('HVP-888', 'IFA', 'blue'),
    ('GTR-232', 'ikarus', '1300');
COMMIT;


SELECT cars.manufacturer, cars.model_name FROM cars;  --list all car manuf and model

BEGIN;  --add new insurance company
INSERT INTO insurance_companies(company_name) VALUES 
    ('otp'),
    ('K&H'),
    ('unicredit');
INSERT INTO services(service_name, salary_limit, valid_years, issuer) 
    SELECT 'paytowin', 2000, 1, MIN(id) FROM insurance_companies; 
INSERT INTO users_services(user_id, services_id) SELECT id, (SELECT id FROM services WHERE id = 1) FROM users WHERE id = 1;
COMMIT;

BEGIN;  --delete cars
    DELETE FROM cars WHERE id=3;
COMMIT;


SELECT * FROM insurance_companies;  -- list all insurance companies

BEGIN;
INSERT INTO insurance_companies(company_name) VALUES ('something');
DELETE FROM insurance_companies WHERE id = 2;
COMMIT;

BEGIN;  --add new service
INSERT INTO services(service_name, salary_limit, valid_years, issuer) VALUES
    ('joy', 200, 1, 1);
COMMIT;

SELECT * FROM services; -- list all services

SELECT * FROM insurance_companies JOIN services ON services.issuer = insurance_companies.id 
WHERE insurance_companies.company_name = 'otp'; 

