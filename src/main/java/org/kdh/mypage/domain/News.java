package org.kdh.mypage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long newsId;

  @Column(nullable = false)
  private String title; // 제목

  @Column(columnDefinition = "TEXT")
  private String summary; // 요약

  @Column(nullable = false)
  private String link; // 링크

  @CreationTimestamp
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime regDate; // 저장한 날짜

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="uid", referencedColumnName = "id", nullable = false)
  private User user;
}
