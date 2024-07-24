create table members(memid serial primary key, surname character varying(200) default 'GUEST', firstname character varying(200) default 'GUEST', address character varying(300) default 'GUEST', zipcode integer default 0, telephone character varying(20), recommendedby integer, joindate timestamp, foreign key(recommendedby) references members(memid));

insert into members (telephone, joindate) values ('000-000-0000', '2012-01-01 00:00:00');
insert into members (surname, firstname, address, zipcode, telephone, joindate) values ('Smith', 'Darren', '8 Bloomsbury Close, Boston', 4321, '555-555-5555', '2012-07-02 10:25:05');
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ('Rumney', 'Henrietta', '3 Bloomsbury Close, Boston', 4321, '555-555-5555', 2, '2012-09-17 13:25:05');
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ('Wick', 'John', 'Paris', 777, '8-800-555-35-35', 2, '2014-09-19 00:00:01');
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ('Jones', 'Douglas', '3 Bloomsbury Close, Boston', 4321, '555-555-5555', 3, '2012-07-02 12:25:05');

select * from members;

select distinct concat(mems.firstname, ' ', mems.surname) as member, (select concat(recs.firstname, ' ', recs.surname) as recommender
			from members recs
			where recs.memid=mems.recommendedby) as recommender
	from members mems
order by member;

drop table members;