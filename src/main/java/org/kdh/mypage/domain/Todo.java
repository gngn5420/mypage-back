package org.kdh.mypage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long todoId; // 할 일 번호

  @Column(nullable = false)
  private String content; // 내용

  @CreationTimestamp
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime regDate; // 등록일

  @UpdateTimestamp
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updateDate; // 업데이트 일

  private boolean complete;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "uid", referencedColumnName = "id", nullable = false)
  private User user; // 사용자 아이디
}
