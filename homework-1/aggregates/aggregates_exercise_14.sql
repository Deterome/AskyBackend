create table members(memid serial primary key, surname character varying(200) default 'GUEST', firstname character varying(200) default 'GUEST', address character varying(300) default 'GUEST', zipcode integer default 0, telephone character varying(20), recommendedby integer, joindate timestamp, foreign key(recommendedby) references members(memid));

insert into members (memid, telephone, joindate) values (0, '000-000-0000', '2012-01-01 00:00:00');
insert into members (surname, firstname, address, zipcode, telephone, joindate) values ('Smith', 'Darren', '8 Bloomsbury Close, Boston', 4321, '555-555-5555', '2012-07-02 10:25:05');
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ('Rumney', 'Henrietta', '3 Bloomsbury Close, Boston', 4321, '555-555-5555', 1, '2012-09-17 13:25:05');
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ('Wick', 'John', 'Paris', 777, '8-800-555-35-35', 1, '2014-09-19 00:00:01');
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ('Jones', 'Douglas', '3 Bloomsbury Close, Boston', 4321, '555-555-5555', 3, '2012-07-02 12:25:05');

create table bookings(bookid serial primary key, facid integer, memid integer, starttime timestamp, slots integer, foreign key(memid) references members(memid));

insert into bookings (facid, memid, starttime, slots) values(2, 2, '2012-08-14 13:30:00', 5);
insert into bookings (facid, memid, starttime, slots) values(3, 1, '2012-09-14 17:30:00', 3);
insert into bookings (facid, memid, starttime, slots) values(1, 3, '2012-09-14 09:00:00', 1);
insert into bookings (facid, memid, starttime, slots) values(1, 2, '2012-10-21 09:00:00', 2);

select * from members;
select * from bookings;

select mems.surname, mems.firstname, bks.memid, min(bks.starttime) as starttime
	from bookings as bks
	inner join members as mems
		on bks.memid=mems.memid
	where date(starttime) >= '2012-09-01'
	group by bks.memid, mems.firstname, mems.surname
order by memid;

drop table bookings;
drop table members;