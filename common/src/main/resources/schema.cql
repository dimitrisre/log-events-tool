CREATE KEYSPACE IF NOT EXISTS pollfish
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 1
};

CREATE TABLE IF NOT EXISTS pollfish.logevents (
    version smallint,
    timestamp bigint,
    message text,
    id uuid,
    PRIMARY KEY (id)
);