insert into review(value, comments, reviewer_id, time_created) values(2, 'Meh', 1, 1565710659);
update review set book_id = 3 where id = 2;
update book set last_modified_by_id = 1, status = 'UNAVAILABLE' where id = 1;
insert into user(id, first_name, last_name, active, online, email, password) values(2, 'John', 'Smith', 1, 1, 'jsmith@gmail.com', '{noop}abcd');
