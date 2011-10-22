create database jobfinder;

CREATE USER 'jobfinder'@'%' IDENTIFIED BY 'jobfinder';
GRANT ALL ON jobfinder.* TO 'jobfinder'@'%';

use jobfinder;

create table job (
	id int,
	min_rate int,
	max_rate int,
	rate_description varchar(1024),
	title varchar(1024),
	description varchar(4080),
	advertiser varchar(1024),
	external_id int,
	search_term varchar(1024)
);
