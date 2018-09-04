package me.ggulmool.todo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Page<Todo> findAll(Pageable pageable);

    Optional<Todo> findById(Long id);

    List<Todo> findByIdIn(List<Long> ids);
}
