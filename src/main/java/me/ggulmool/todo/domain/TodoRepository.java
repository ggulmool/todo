package me.ggulmool.todo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Page<Todo> findAll(Pageable pageable);

    @Query("SELECT t, count(p) " +
            " FROM Todo t LEFT OUTER JOIN t.parentTodos p " +
            " GROUP BY t")
    List<Object[]> findAllTodos(Pageable page);

    Optional<Todo> findById(Long id);

    List<Todo> findByIdIn(List<Long> ids);

    @Query(value = "SELECT t, p FROM Todo t JOIN t.parentTodos p ON t.id = p.id WHERE p.id = ?1")
    List<Todo> getParentTodosById(long todoId);
}
