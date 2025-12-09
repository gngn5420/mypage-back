package org.kdh.mypage.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoUpdateDTO {
  private String content; // 수정된 할 일 내용
}
