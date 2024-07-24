create table facilities(facid serial primary key, name character varying(100), membercost numeric, guestcost numeric, initialoutlay numeric, monthlymaintenance numeric);

insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values('Tennis Court 1', 5, 25, 10000, 200);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values('Tennis Court 2', 10, 35, 15000, 300);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values('Badminton Court', 0, 15.5, 4000, 50);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values('Table Tennis', 0, 5, 320, 10);

create table members(memid serial primary key, surname character varying(200), firstname character varying(200), address character varying(300), zipcode integer, telephone character varying(20), recommendedby integer, joindate timestamp, foreign key(recommendedby) references members(memid));

insert into members (surname, firstname, address, zipcode, telephone, joindate) values ('Smith', 'Darren', '8 Bloomsbury Close, Boston', 4321, '555-555-5555', '2012-07-02 10:25:05');
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ('Rumney', 'Henrietta', '3 Bloomsbury Close, Boston', 4321, '555-555-5555', 1, '2012-09-17 13:25:05');
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ('Wick', 'John', 'Paris', 777, '8-800-555-35-35', 1, '2014-09-19 00:00:01');

create table bookings(bookid serial primary key, facid integer, memid integer, starttime timestamp, slots integer, foreign key(facid) references facilities(facid), foreign key(memid) references members(memid));

insert into bookings (facid, memid, starttime, slots) values(2, 2, '2012-09-18 13:30:00', 1);
insert into bookings (facid, memid, starttime, slots) values(3, 1, '2012-09-18 17:30:00', 3);
insert into bookings (facid, memid, starttime, slots) values(1, 2, '2012-09-21 09:00:00', 2);
insert into bookings (facid, memid, starttime, slots) values(1, 3, '2012-09-21 10:00:00', 5);

select * from facilities;
select * from members;
select * from bookings;

select distinct concat(mems.firstname, ' ', mems.surname) as member, facs.name as facility
	from members mems
		inner join bookings bks
			on bks.memid=mems.memid
		inner join facilities facs
			on facs.facid=bks.facid
	where facs.name like 'Tennis Court%'
order by member, facility;

drop table bookings;
drop table members;
drop table facilities;