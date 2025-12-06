package org.kdh.mypage.repository;

import org.kdh.mypage.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
  @Query("select m from Memo m where m.user.username = :username")
  List<Memo> findAllByContent(@Param("username") String username);
  // Memo에서 username과 같은 모든 레코드를 가져오는 코드
}
