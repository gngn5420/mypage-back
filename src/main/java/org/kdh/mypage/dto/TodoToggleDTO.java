package org.kdh.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoToggleDTO {
  private Long todoId;   // 할 일 항목의 고유 ID
  private boolean isDone; // 완료 여부
}
