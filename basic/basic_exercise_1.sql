create table facilities(facid integer auto_increment primary key, name character varying(100), membercost numeric, guestcost numeric, initialoutlay numeric, monthlymaintenance numeric);

insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Tennis Court 1", 5, 25, 10000, 200);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Tennis Court 2", 5, 25, 8000, 200);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Badminton Court", 0, 15.5, 4000, 50);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Table Tennis", 0, 5, 320, 10);

select * from facilities;

drop table facilities;