package org.kdh.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.kdh.mypage.dto.NewsDTO;
import org.kdh.mypage.security.UserPrincipal;
import org.kdh.mypage.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController // 오직 JSON 데이터만 반호나 (html 페이지 반환x)
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

  private final NewsService newsService;

  // 뉴스 저장
  @PostMapping("/save")
  public ResponseEntity<?> saveNews(@AuthenticationPrincipal UserPrincipal user,
                                    @RequestBody NewsDTO dto) {
    return ResponseEntity.ok(newsService.saveNews(dto, user.getUsername()));
  }
  // 뉴스 목록 가져오기
  @GetMapping("/list")
  public ResponseEntity<?> getNewsList(@AuthenticationPrincipal UserPrincipal user) {
    return ResponseEntity.ok(newsService.getNewsList(user.getUsername()));
  }

  // 뉴스 삭제하기
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteNews(@AuthenticationPrincipal UserPrincipal user,
                                      @PathVariable Long id) {
    newsService.deleteNews(id, user.getUsername());
    return ResponseEntity.ok(true);
  }
}
