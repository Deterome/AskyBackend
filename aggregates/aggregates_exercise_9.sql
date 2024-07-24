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

select facs.name, sum(bks.slots * case
					 when bks.memid=0 then facs.guestcost
					 else facs.membercost
					end) as revenue
	from bookings bks
		inner join facilities facs
			on facs.facid=bks.facid
	group by facs.name
order by revenue;

drop table bookings;
drop table facilities;