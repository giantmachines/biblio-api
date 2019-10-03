insert into author(id, first_name) values(1, 'Homer');
insert into author(id, first_name, last_name) values(2, 'Martin', 'Fowler');
insert into author(id, first_name, last_name) values(3, 'Eric', 'Gamma');
insert into author(id, first_name, last_name) values(4, 'Harold', 'Camping');

insert into user(id, first_name, last_name, active, online, email, password) values(1, 'Philip', 'Ford', 1, 1, 'paford@gmail.com', '{noop}1234');


insert into book(id, image, title, author_id, status, time_created, last_updated)
    values(1, 'http://localhost/biblio/books/images/1', 'The Iliad', 1, 'UNAVAILABLE', 156571065900, 156571065900);
insert into book(id, image, title, author_id, time_created, last_updated)
    values(2, 'http://localhost/biblio/books/images/2', 'Patterns of Enterprise Software', 2, 156571065900, 156571065900);
insert into book(id, image, title, author_id, time_created, last_updated)
    values(3, 'http://localhost/biblio/books/images/3', 'Refactoring', 2, 156571065900, 156571065900);
insert into book(id, image, title, author_id, time_created, last_updated)
    values(4, 'http://localhost/biblio/books/images/4', 'Design Patterns', 3, 156571065900, 156571065900);
insert into book(id, image, title, author_id, status, time_created, last_updated)
    values(5, 'http://localhost/biblio/books/images/5', '1994', 4, 'DEACTIVATED', 156571065900, 156571065900);


insert into review(id, value, reviewer_id, comments, time_created, last_updated)
    values(1, 5, 1, 'I liked it.', 1565815231, 1565815231);
update review set book_id = 1 where id = 1;

update book set last_modified_by_id = 1 where id = 1;
