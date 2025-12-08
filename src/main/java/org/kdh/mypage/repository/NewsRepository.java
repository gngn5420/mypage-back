package org.kdh.mypage.repository;

import org.kdh.mypage.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

  List<News> findByUserUsername(String username);
}
