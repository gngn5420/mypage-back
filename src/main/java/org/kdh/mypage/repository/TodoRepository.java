package org.kdh.mypage.repository;

import org.kdh.mypage.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
  // 사용자별 Todo 목록 조회
  List<Todo> findByUserUsername(String username);
}
