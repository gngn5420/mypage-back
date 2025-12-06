package org.kdh.mypage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long todoId;
  @Column(nullable = false)
  private String content;
  @CreationTimestamp
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime regDate;
  @CreationTimestamp
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime updateDate;
  private boolean is_complete;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="uid", referencedColumnName = "id", updatable = false, nullable = false)
  private User user;

}
