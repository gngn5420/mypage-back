package org.kdh.mypage.repository;

import org.kdh.mypage.domain.Todo;
import org.kdh.mypage.repository.projection.TodoCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
  // 사용자별 Todo 목록 조회
  List<Todo> findByUserUsername(String username);

  // admin 페이지 회원 정보 출력용
  @Query("""
        select t.user.id as userId, count(t) as cnt
        from Todo t
        where t.user.id in :userIds
        group by t.user.id
    """)
  List<TodoCountProjection> countByUserIds(@Param("userIds") List<Long> userIds);
}
