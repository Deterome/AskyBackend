create table facilities(facid serial primary key, name character varying(100), membercost numeric, guestcost numeric, initialoutlay numeric, monthlymaintenance numeric);

insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values('Tennis Court 1', 5, 25, 10000, 200);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values('Tennis Court 2', 10, 35, 15000, 300);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values('Badminton Court', 0, 15.5, 4000, 50);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values('Table Tennis', 0, 5, 320, 10);

create table bookings(bookid serial primary key, facid integer, memid integer, starttime timestamp, slots integer, foreign key(facid) references facilities(facid));

insert into bookings (facid, memid, starttime, slots) values(2, 2, '2012-09-14 13:30:00', 5);
insert into bookings (facid, memid, starttime, slots) values(3, 1, '2012-09-14 17:30:00', 3);
insert into bookings (facid, memid, starttime, slots) values(1, 3, '2012-09-14 09:00:00', 1);
insert into bookings (facid, memid, starttime, slots) values(1, 2, '2012-09-21 09:00:00', 2);

select * from facilities;
select * from bookings;

select bks.facid, facs.name, trim(to_char(sum(bks.slots)/2.0, '999999D99')) as "Total Hours"
	from bookings as bks
	inner join facilities as facs
		on bks.facid=facs.facid
	group by bks.facid, facs.name
order by facid;

drop table bookings;
drop table facilities;