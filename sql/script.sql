-- Drop tables if they exist
DROP TABLE IF EXISTS public.directorate CASCADE;
DROP TABLE IF EXISTS public.department CASCADE;
DROP TABLE IF EXISTS public.employee CASCADE;

-- Drop sequences if they exist
DROP SEQUENCE IF EXISTS public.directorate_seq CASCADE;
DROP SEQUENCE IF EXISTS public.department_seq CASCADE;
DROP SEQUENCE IF EXISTS public.employee_seq CASCADE;

-- Create sequences with increment size of 1
CREATE SEQUENCE public.directorate_seq INCREMENT BY 1;
CREATE SEQUENCE public.department_seq INCREMENT BY 1;
CREATE SEQUENCE public.employee_seq INCREMENT BY 1;

-- Create table for directorates
CREATE TABLE public.directorate (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('public.directorate_seq'),
    active BOOLEAN NOT NULL,
    director_id BIGINT UNIQUE,
    description VARCHAR(255),
    name VARCHAR(255)
);

-- Create table for departments
CREATE TABLE public.department (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('public.department_seq'),
    active BOOLEAN NOT NULL,
    directorate_id BIGINT,
    description VARCHAR(255),
    name VARCHAR(255),
    CONSTRAINT fk_department_directorate FOREIGN KEY (directorate_id)
        REFERENCES public.directorate (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- Create table for employees
CREATE TABLE public.employee (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('public.employee_seq'),
    active BOOLEAN NOT NULL,
    age INTEGER,
    department_id BIGINT,
    name VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    personal_id VARCHAR(255) NOT NULL UNIQUE,
    position VARCHAR(255) NOT NULL CHECK (position IN ('DIRECTORATE_DIRECTOR', 'DEPARTMENT_HEAD', 'EMPLOYEE')),
    surname VARCHAR(255),
    CONSTRAINT fk_employee_department FOREIGN KEY (department_id)
        REFERENCES public.department (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- Add foreign key constraint to directorates referencing employees
ALTER TABLE public.directorate
    ADD CONSTRAINT fk_directorate_employee FOREIGN KEY (director_id)
    REFERENCES public.employee (id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

-- Set ownership of tables to postgres user
ALTER TABLE public.directorate OWNER TO postgres;
ALTER TABLE public.department OWNER TO postgres;
ALTER TABLE public.employee OWNER TO postgres;
