DROP FUNCTION clean_up(character varying);
CREATE OR REPLACE FUNCTION clean_up(character varying) RETURNS integer AS $$
DECLARE
	maxd timestamp;
BEGIN
	SELECT into maxd max(updated) from factoids WHERE name=$1;
	DELETE from factoids where name=$1 and updated < maxd;

	RETURN 1;
END;
$$ LANGUAGE 'plpgsql';

select name, value, clean_up(f.name)
	from factoids f
	where name like 'karma %'
	order by name;
