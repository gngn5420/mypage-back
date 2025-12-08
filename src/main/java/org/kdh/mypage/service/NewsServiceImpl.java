package org.kdh.mypage.service;

import lombok.RequiredArgsConstructor;
import org.kdh.mypage.domain.News;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.dto.NewsDTO;
import org.kdh.mypage.repository.NewsRepository;
import org.kdh.mypage.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {

  private final NewsRepository newsRepository;
  private final UserRepository userRepository;

  @Override
  public NewsDTO saveNews(NewsDTO dto, String username) {

//    User user = userRepository.findByUsername(username);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (user == null)
      throw new RuntimeException("User not found");

    News news = News.builder()
        .title(dto.getTitle())
        .summary(dto.getSummary())
        .link(dto.getLink())
        .user(user)
        .build();

    News saved = newsRepository.save(news);

    return entityToDTO(saved);
  }

  @Override
  public List<NewsDTO> getNewsList(String username) {
    List<News> list = newsRepository.findByUserUsername(username);
    return list.stream().map(this::entityToDTO).collect(Collectors.toList());
  }

  @Override
  public void deleteNews(Long id, String username) {

    News news = newsRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("News not found"));

    if (!news.getUser().getUsername().equals(username))
      throw new RuntimeException("권한 없음");

    newsRepository.delete(news);
  }

  // -----------------------
  // 변환 함수
  // -----------------------
  private NewsDTO entityToDTO(News news) {
    return NewsDTO.builder()
        .newsId(news.getNewsId())
        .username(news.getUser().getUsername())
        .title(news.getTitle())
        .summary(news.getSummary())
        .link(news.getLink())
        .regDate(news.getRegDate())
        .build();
  }
}
