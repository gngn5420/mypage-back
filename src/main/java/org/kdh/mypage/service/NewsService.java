package org.kdh.mypage.service;

import org.kdh.mypage.dto.NewsDTO;
import java.util.List;

public interface NewsService {
  NewsDTO saveNews(NewsDTO dto, String username);
  List<NewsDTO> getNewsList(String username);
  void deleteNews(Long id, String username);
}
