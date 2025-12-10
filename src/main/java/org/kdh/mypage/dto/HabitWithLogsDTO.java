package org.kdh.mypage.dto;

import lombok.*;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 습관별 체크 표시 목록
public class HabitWithLogsDTO { // 습관에 대한 한 주 기록
  private Long habitId;
  private String name;
  private String emoji;

  //체크 여부
  private Map<String, Boolean> logs;
}
