package me.ggulmool.todo.domain.support;

import me.ggulmool.todo.domain.Todo;
import me.ggulmool.todo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Page<Todo> findTodoByUser(Pageable pageable, User user);

    Optional<Todo> findTodoByIdAndUser(Long id, User user);

    List<Todo> findTodoByIdInAndUser(List<Long> ids, User user);
}
