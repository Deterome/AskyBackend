create table facilities(facid integer auto_increment primary key, name character varying(100), membercost numeric, guestcost numeric, initialoutlay numeric, monthlymaintenance numeric);

insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Tennis Court 1", 5, 25, 10000, 200);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Badminton Court", 0, 15.5, 4000, 50);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Table Tennis", 0, 5, 320, 10);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Massage Room 1", 35, 1, 10000, 3000);

create table members(memid integer auto_increment primary key, surname character varying(200), firstname character varying(200), address character varying(300), zipcode integer, telephone character varying(20), recommendedby integer, joindate timestamp, foreign key(recommendedby) references members(memid));

insert into members (surname, firstname, address, zipcode, telephone, joindate) values ("Farrell", "David", "5 Bloomsbury Close, Boston", 4321, "777-777-7777", "2012-08-19 10:05:21");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Smith", "Darren", "8 Bloomsbury Close, Boston", 4321, "555-555-5555", 1, "2012-07-02 10:25:05");

create table bookings(bookid integer auto_increment primary key, facid integer, memid integer, starttime timestamp, slots integer, foreign key(facid) references facilities(facid), foreign key(memid) references members(memid));

insert into bookings (facid, memid, starttime, slots) values(1, 2, '2012-09-18 09:00:00', 2);
insert into bookings (facid, memid, starttime, slots) values(2, 2, '2012-09-18 13:30:00', 1);
insert into bookings (facid, memid, starttime, slots) values(3, 1, '2012-09-18 17:30:00', 3);

select * from facilities;
select * from members;
select * from bookings;

select bks.starttime 
	from
		bookings bks
		inner join members mems
			on bks.memid=mems.memid
	where 
		mems.surname = 'Farrell'
		and mems.firstname = 'David';

drop table bookings;
drop table members;
drop table facilities;