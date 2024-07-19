create table facilities(facid integer auto_increment primary key, name character varying(100), membercost numeric, guestcost numeric, initialoutlay numeric, monthlymaintenance numeric);

insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Tennis Court 1", 5, 25, 10000, 200);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Tennis Court 2", 5, 25, 8000, 200);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Badminton Court", 0, 15.5, 4000, 50);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Table Tennis", 0, 5, 320, 10);
insert into facilities (name, membercost, guestcost, initialoutlay, monthlymaintenance) values("Massage Room 1", 35, 1, 10000, 3000);


create table members(memid integer auto_increment primary key, surname character varying(200), firstname character varying(200), address character varying(300), zipcode integer, telephone character varying(20), recommendedby integer, joindate timestamp);

insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Smith", "Darren", "8 Bloomsbury Close, Boston", 4321, "555-555-5555", 1, "2012-07-02 10:25:05");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Jones", "Douglas", "3 Bloomsbury Close, Boston", 4321, "555-555-5555", 2, "2012-07-02 12:25:05");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Rumney", "Henrietta", "3 Bloomsbury Close, Boston", 4321, "555-555-5555", 5, "2012-09-17 13:25:05");
insert into members (surname, firstname, address, zipcode, telephone, recommendedby, joindate) values ("Wick", "John", "Paris", 777, "8-800-555-35-35", 100, "2014-09-19 00:00:01");

select * from facilities;
select * from members;

select surname from members union select name as surname from facilities;

drop table members;
drop table facilities;