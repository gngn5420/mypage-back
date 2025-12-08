package org.kdh.mypage.repository;

import org.kdh.mypage.domain.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
// habit 자체 정보 (name, emoji, user)를 crud 하는 곳
public interface HabitRepository extends JpaRepository<Habit, Long> {

  List<Habit> findByUserUsername(String username);
}
