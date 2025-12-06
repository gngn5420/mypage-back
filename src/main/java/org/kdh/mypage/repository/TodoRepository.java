package org.kdh.mypage.repository;

import org.kdh.mypage.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
  @Query("select todo from Todo todo where todo.user.username=:username")
  List<Todo> findAllByContent(@Param("username") String username);
  // TodoList에서 user.username이 파라미터 username과 같은 모든 레코드를 가져온다.
}
