package org.kdh.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitLogDTO {
  private Long logId; // 기록 순서
  private Long habitId; // 어떤 습관인지
  private String date; // 날짜
  private boolean checked; // 체크 표시
}
