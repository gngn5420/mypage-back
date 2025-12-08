package org.kdh.mypage.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long logId;

  @Column(nullable = false)
  private String date; // YYYY-MM-DD

  @Column(nullable = false)
  private boolean checked; // 체크 표시

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "habit_id", nullable = false)
  private Habit habit;
}
