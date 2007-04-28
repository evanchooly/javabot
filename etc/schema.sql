CREATE SEQUENCE factoid_sequence;
CREATE TABLE factoids(
	id BIGINT NOT NULL,
	name VARCHAR(255) NOT NULL,
	value VARCHAR(2000) NOT NULL,
	username VARCHAR(100) NOT NULL,
	updated TIMESTAMP
	);
ALTER TABLE factoids ALTER ID SET DEFAULT NEXTVAL('factoid_sequence');

CREATE TABLE changes(
	message VARCHAR(4000) NOT NULL,
	changeDate TIMESTAMP
	);

CREATE TABLE seen(
	id BIGINT NOT NULL,
	nick VARCHAR(255) NOT NULL,
	channel VARCHAR(255) NOT NULL,
	message VARCHAR(4000) NOT NULL,
	updated TIMESTAMP
);

CREATE TABLE logs(
	nick VARCHAR(255) NOT NULL,
        channel VARCHAR(255) NOT NULL,
        message VARCHAR(4000) NOT NULL,
        updated TIMESTAMP
);
