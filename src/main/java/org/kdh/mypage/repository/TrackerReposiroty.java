package org.kdh.mypage.repository;

import org.kdh.mypage.domain.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackerReposiroty extends JpaRepository<Habit,Long> {
  @Query("select hb from Habit hb where hb.user.username = :username")
  List<Habit> findAllByContent(@Param("username") String username);
}
