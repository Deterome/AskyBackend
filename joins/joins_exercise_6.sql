create table facilities(facid integer auto_increment primary key, name character varying(100), membercost numeric, guestcost numeric, initialoutlay numeric, monthlymaintenance numeric);

insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Tennis Court 1", 5, 25, 10000, 200);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Tennis Court 2", 10, 35, 15000, 300);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Badminton Court", 0, 15.5, 4000, 50);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Table Tennis", 0, 5, 320, 10);

create table members(memid integer auto_increment primary key, surname character varying(200) default 'GUEST', firstname character varying(200) default 'GUEST', address character varying(300) default 'GUEST', zipcode integer default 0, telephone character varying(20), recommendedby integer, joindate timestamp, foreign key(recommendedby) references members(memid));

insert into members (telephone, joindate) values ("000-000-0000", "2012-01-01 00:00:00");
insert into members (surname, firstname, address, zipcode, telephone, joindate) values ("Smith", "Darren", "8 Bloomsbury Close, Boston", 4321, "555-555-5555", "2012-07-02 10:25:05");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Rumney", "Henrietta", "3 Bloomsbury Close, Boston", 4321, "555-555-5555", 1, "2012-09-17 13:25:05");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Wick", "John", "Paris", 777, "8-800-555-35-35", 1, "2014-09-19 00:00:01");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Jones", "Douglas", "3 Bloomsbury Close, Boston", 4321, "555-555-5555", 3, "2012-07-02 12:25:05");

create table bookings(bookid integer auto_increment primary key, facid integer, memid integer, starttime timestamp, slots integer, foreign key(facid) references facilities(facid), foreign key(memid) references members(memid));

insert into bookings (facid, memid, starttime, slots) values(2, 2, '2012-09-14 13:30:00', 5);
insert into bookings (facid, memid, starttime, slots) values(3, 1, '2012-09-14 17:30:00', 3);
insert into bookings (facid, memid, starttime, slots) values(1, 3, '2012-09-14 09:00:00', 1);
insert into bookings (facid, memid, starttime, slots) values(1, 2, '2012-09-21 09:00:00', 2);

select * from facilities;
select * from members;
select * from bookings;

select concat(mems.firstname, ' ', mems.surname) as member, facs.name as facility, (case 
		when mems.firstname='GUEST' then bks.slots*facs.guestcost
		else bks.slots*facs.membercost
end) as cost
	from members mems
		inner join bookings bks
			on bks.memid=mems.memid
		inner join facilities facs
			on facs.facid=bks.facid
	where
		date(bks.starttime)='2012-09-14'
		and (case 
				when mems.firstname='GUEST' then bks.slots*facs.guestcost
				else bks.slots*facs.membercost
			end)>30
	order by cost desc; 

drop table bookings;
drop table members;
drop table facilities;