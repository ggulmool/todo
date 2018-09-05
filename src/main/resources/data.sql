insert into user(user_id, name, password) values('ggulmool', '김남열', 'test1234');
insert into todo(todo_id, contents, status, display_contents, user_id, created_date, last_modified_date) values(1L, '집안일', 'DOING', '집안일', 'ggulmool', '2018-09-02 23:08:42.856','2018-09-02 23:08:42.856');
insert into todo(todo_id, contents, status, display_contents, user_id, created_date, last_modified_date) values(2L, '빨래', 'DOING', '빨래 @1', 'ggulmool', '2018-09-02 23:08:42.857','2018-09-02 23:08:42.857');
insert into todo(todo_id, contents, status, display_contents, user_id, created_date, last_modified_date) values(3L, '청소', 'DOING', '청소 @1', 'ggulmool', '2018-09-02 23:08:42.858','2018-09-02 23:08:42.858');
insert into todo(todo_id, contents, status, display_contents, user_id, created_date, last_modified_date) values(4L, '방청소', 'DOING', '방청소 @1 @3', 'ggulmool', '2018-09-02 23:08:42.859','2018-09-02 23:08:42.859');
insert into todo_parent_ref (todo_id, parent_id) values(2L, 1L);
insert into todo_parent_ref (todo_id, parent_id) values(3L, 1L);
insert into todo_parent_ref (todo_id, parent_id) values(4L, 1L);
insert into todo_parent_ref (todo_id, parent_id) values(4L, 3L);
