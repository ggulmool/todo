insert into todo(todo_id, contents, status) values(1L, '집안일', 'DOING');
insert into todo(todo_id, contents, status) values(2L, '빨래', 'DOING');
insert into todo(todo_id, contents, status) values(3L, '청소', 'DOING');
insert into todo(todo_id, contents, status) values(4L, '방청소', 'DOING');
insert into todo_parent_ref (todo_id, parent_id) values(2L, 1L);
insert into todo_parent_ref (todo_id, parent_id) values(3L, 1L);
insert into todo_parent_ref (todo_id, parent_id) values(4L, 1L);
insert into todo_parent_ref (todo_id, parent_id) values(4L, 3L);
insert into user(user_id, name) values(1L, '김남열');