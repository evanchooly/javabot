alter table classes drop constraint fk32e13826386ea614;
delete from methods;
delete from classes;
delete from apis;
\i /tmp/apis.dump
\i /tmp/classes.dump
\i /tmp/methods.dump
ALTER TABLE classes ADD CONSTRAINT fk32e13826386ea614 FOREIGN KEY (superclass_id) REFERENCES classes(id);

