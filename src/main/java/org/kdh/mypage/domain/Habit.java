package org.kdh.mypage.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Habit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long habitId;
  @Column(nullable = false)
  private String content;
  private boolean is_checked;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="uid", referencedColumnName = "id", updatable = false, nullable = false)
  private User user;

}
