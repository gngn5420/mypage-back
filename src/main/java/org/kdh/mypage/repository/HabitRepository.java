package org.kdh.mypage.repository;

import org.kdh.mypage.domain.Habit;
import org.kdh.mypage.repository.projection.HabitCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// habit 자체 정보 (name, emoji, user)를 crud 하는 곳
public interface HabitRepository extends JpaRepository<Habit, Long> {
  // 유저 이름으로 습관 리스트 불러오기
  List<Habit> findByUserUsername(String username);

  // admin 페이지 회원 목록 출력용
  @Query("""
        select h.user.id as userId, count(h) as cnt
        from Habit h
        where h.user.id in :userIds
        group by h.user.id
    """)
  List<HabitCountProjection> countByUserIds(@Param("userIds") List<Long> userIds);

  // 탈퇴 시 FK부터 삭제 되도록
  @Modifying
  @Query("delete from Habit h where h.user.id = :userId")
  void deleteByUserId(@Param("userId") Long userId);


}
