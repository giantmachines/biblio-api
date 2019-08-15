insert into author(id, first_name) values(1, 'Homer');
insert into author(id, first_name, last_name) values(2, 'Martin', 'Fowler');
insert into author(id, first_name, last_name) values(3, 'Eric', 'Gamma');
insert into author(id, first_name, last_name) values(4, 'Harold', 'Camping');

insert into user(id, first_name, last_name, active, online, email, password) values(1, 'Philip', 'Ford', 1, 1, 'paford@gmail.com', '{noop}1234');

insert into book(id, image, title, author_id) values(1, 'http://localhost/biblio/books/images/1', 'The Iliad', 1);
insert into book(id, image, title, author_id) values(2, 'http://localhost/biblio/books/images/2', 'Patterns of Enterprise Software', 2);
insert into book(id, image, title, author_id) values(3, 'http://localhost/biblio/books/images/3', 'Refactoring', 2);
insert into book(id, image, title, author_id) values(4, 'http://localhost/biblio/books/images/4', 'Design Patterns', 3);
insert into book(id, image, title, author_id) values(5, 'http://localhost/biblio/books/images/5', '1994', 4);

insert into book_status(id, value, user_id, book_id, last_updated) values(1, 0, 1, 1, 1565710659);
insert into book_status(id, value, user_id, book_id, last_updated) values(2, 1, null, 2, 1565710659);
insert into book_status(id, value, user_id, book_id, last_updated) values(3, 1, null, 3, 1565710659);
insert into book_status(id, value, user_id, book_id, last_updated) values(4, 1, null, 4, 1565710659);
insert into book_status(id, value, user_id, book_id, last_updated) values(5, 4, null, 5, 1565710659);

insert into review(id, value, reviewer_id, comments, time_created) values(1, 5, 1, 'I liked it.', 1565815231);
insert into book_reviews (book_id, reviews_id) values(1, 1);