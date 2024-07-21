create table members(memid integer auto_increment primary key, surname character varying(200), firstname character varying(200), address character varying(300), zipcode integer, telephone character varying(20), recommendedby integer, joindate timestamp, foreign key(recommendedby) references members(memid));

insert into members (surname, firstname, address, zipcode, telephone, joindate) values ("Smith", "Darren", "8 Bloomsbury Close, Boston", 4321, "555-555-5555", "2012-07-02 10:25:05");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Rumney", "Henrietta", "3 Bloomsbury Close, Boston", 4321, "555-555-5555", 1, "2012-09-17 13:25:05");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Wick", "John", "Paris", 777, "8-800-555-35-35", 1, "2014-09-19 00:00:01");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Jones", "Douglas", "3 Bloomsbury Close, Boston", 4321, "555-555-5555", 3, "2012-07-02 12:25:05");

select * from members;

select mems.firstname as memfname, mems.surname as memsname, recs.firstname as recfname, recs.surname as recsname
	from members mems
		left join members recs
		on mems.recommendedby=recs.memid
	order by mems.surname, mems.firstname;

drop table members;