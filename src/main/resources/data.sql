insert into author(id, first_name) values(1, 'Homer');
insert into author(id, first_name, last_name) values(2, 'Martin', 'Fowler');
insert into author(id, first_name, last_name) values(3, 'Eric', 'Gamma');

insert into user(id, first_name, last_name, active, online) values(1, 'Philip', 'Ford', 1, 1);

insert into book(id, image, title, author_id) values(1, 'http://localhost/biblio/books/images/1', 'The Iliad', 1);
insert into book(id, image, title, author_id) values(2, 'http://localhost/biblio/books/images/2', 'Patterns of Enterprise Software', 2);
insert into book(id, image, title, author_id) values(3, 'http://localhost/biblio/books/images/3', 'Refactoring', 2);
insert into book(id, image, title, author_id) values(4, 'http://localhost/biblio/books/images/4', 'Design Patterns', 3);

insert into book_status(id, value, user_id) values(1, 0, 1);
insert into book_status(id, value, user_id) values(2, 1, null);
insert into book_status(id, value, user_id) values(3, 1, null);
insert into book_status(id, value, user_id) values(4, 1, null);

insert into review(id, value, reviewer_id, comments) values(1, 5, 1, 'I liked it.');
insert into book_reviews (book_id, reviews_id) values(1, 1);