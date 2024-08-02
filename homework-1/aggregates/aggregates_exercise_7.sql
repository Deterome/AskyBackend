create table bookings(bookid serial primary key, facid integer, memid integer, starttime timestamp, slots integer);

insert into bookings (facid, memid, starttime, slots) values(2, 2, '2012-09-14 13:30:00', 5);
insert into bookings (facid, memid, starttime, slots) values(3, 1, '2012-09-14 17:30:00', 3);
insert into bookings (facid, memid, starttime, slots) values(1, 3, '2012-10-14 09:00:00', 1);
insert into bookings (facid, memid, starttime, slots) values(1, 2, '2013-10-21 09:00:00', 2);

select * from bookings;

select count(distinct memid)
	from bookings;

drop table bookings;