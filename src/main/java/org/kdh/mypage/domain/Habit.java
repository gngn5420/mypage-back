package org.kdh.mypage.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Habit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long habitId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String emoji; // 이모지

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "uid", referencedColumnName = "id", nullable = false, updatable = false)
  private User user;

  // cascade -> 부모 삭제, 자식 삭제 -> 자동 연쇄 처리
  // ohphanRemoval -> 부모 컬렉션에서 빠진 고아 로그를 자동 삭제
  @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<HabitLog> logs = new ArrayList<>();
}
