create table facilities(facid integer auto_increment primary key, name character varying(100), membercost numeric, guestcost numeric, initialoutlay numeric, monthlymaintenance numeric);

insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Tennis Court 1", 5, 25, 10000, 200);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Tennis Court 2", 10, 35, 15000, 300);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Badminton Court", 0, 15.5, 4000, 50);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Table Tennis", 0, 5, 320, 10);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Massage Room 1", 35, 1, 10000, 3000);

create table bookings(bookid integer auto_increment primary key, facid integer, memid integer, starttime timestamp, slots integer, foreign key(facid) references facilities(facid));

insert into bookings (facid, memid, starttime, slots) values(2, 2, '2012-09-18 13:30:00', 1);
insert into bookings (facid, memid, starttime, slots) values(3, 1, '2012-09-18 17:30:00', 3);
insert into bookings (facid, memid, starttime, slots) values(1, 2, '2012-09-21 09:00:00', 2);
insert into bookings (facid, memid, starttime, slots) values(1, 3, '2012-09-21 10:00:00', 5);
insert into bookings (facid, memid, starttime, slots) values(2, 4, '2012-09-21 12:00:00', 1);

select * from facilities;
select * from bookings;

select bks.starttime as start, fcs.name 
	from bookings bks
		inner join facilities fcs
			on bks.facid=fcs.facid
	where 
		fcs.name like 'Tennis Court%'
		and date(bks.starttime)='2012-09-21'
	order by starttime;

drop table bookings;
drop table facilities;