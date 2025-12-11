package org.kdh.mypage.repository;

import org.kdh.mypage.domain.Habit;
import org.kdh.mypage.domain.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
 // 습관, 날짜 -> 날짜에 해당하는 습관로그 다 가져오기
  Optional<HabitLog> findByHabitAndDate(Habit habit, String date);

  // 습관 삭제 전에 로그 선삭제
  void deleteByHabit(Habit habit);
  // 습관 id 기반 삭제
  void deleteByHabit_HabitId(Long habitId);


}
