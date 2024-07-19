create table members(memid integer auto_increment primary key, surname character varying(200), firstname character varying(200), address character varying(300), zipcode integer, telephone character varying(20), recommendedby integer, joindate timestamp);

insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Smith", "Darren", "8 Bloomsbury Close, Boston", 4321, "555-555-5555", 1, "2012-07-02 10:25:05");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Jones", "Douglas", "3 Bloomsbury Close, Boston", 4321, "555-555-5555", 2, "2012-07-02 12:25:05");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Rumney", "Henrietta", "3 Bloomsbury Close, Boston", 4321, "555-555-5555", 5, "2012-09-17 13:25:05");

select * from members;

select memid, surname, firstname, joindate from members where joindate >= '2012-09-01';

drop table members;