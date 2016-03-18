--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.1
-- Dumped by pg_dump version 9.5.1

-- Started on 2016-03-18 14:19:01 GMT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2117 (class 1262 OID 16384)
-- Name: swarmapp; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE swarmapp WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8';


ALTER DATABASE swarmapp OWNER TO postgres;

\connect swarmapp

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 7 (class 2615 OID 16385)
-- Name: security; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA security;


ALTER SCHEMA security OWNER TO postgres;

--
-- TOC entry 1 (class 3079 OID 12361)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2120 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 182 (class 1259 OID 16386)
-- Name: payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE payments (
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    phone character varying(20) NOT NULL,
    total_amount numeric NOT NULL,
    deducted_fee_amount numeric NOT NULL,
    receiver_location_id integer NOT NULL,
    sender_location_id integer NOT NULL,
    is_completed boolean DEFAULT false,
    passport_id character varying(100),
    id character varying(50) NOT NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    completed timestamp with time zone
);


ALTER TABLE payments OWNER TO postgres;

SET search_path = security, pg_catalog;

--
-- TOC entry 183 (class 1259 OID 16394)
-- Name: seq_user_id; Type: SEQUENCE; Schema: security; Owner: postgres
--

CREATE SEQUENCE seq_user_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 10;


ALTER TABLE seq_user_id OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 16396)
-- Name: users; Type: TABLE; Schema: security; Owner: postgres
--

CREATE TABLE users (
    id integer DEFAULT nextval('seq_user_id'::regclass) NOT NULL,
    email character varying(50),
    firstname character varying(20),
    lastname character varying(20),
    passwd character varying(20) NOT NULL
);


ALTER TABLE users OWNER TO postgres;

SET search_path = public, pg_catalog;

--
-- TOC entry 1995 (class 2606 OID 16401)
-- Name: pk_txid; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY payments
    ADD CONSTRAINT pk_txid PRIMARY KEY (id);


SET search_path = security, pg_catalog;

--
-- TOC entry 1998 (class 2606 OID 16403)
-- Name: pk_user_id; Type: CONSTRAINT; Schema: security; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT pk_user_id PRIMARY KEY (id);


--
-- TOC entry 1996 (class 1259 OID 16404)
-- Name: idx_username; Type: INDEX; Schema: security; Owner: postgres
--

CREATE INDEX idx_username ON users USING btree (email);


--
-- TOC entry 2119 (class 0 OID 0)
-- Dependencies: 8
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-03-18 14:19:01 GMT

--
-- PostgreSQL database dump complete
--

