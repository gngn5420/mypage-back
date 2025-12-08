package org.kdh.mypage.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {
  private Long todoId;
  private String username;
  private String content;
  private LocalDateTime regDate;
  private LocalDateTime updateDate;
  private boolean complete;
}
