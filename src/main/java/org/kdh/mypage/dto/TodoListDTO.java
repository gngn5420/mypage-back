package org.kdh.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoListDTO {
  private Long todoId;
  private String username;
  private String content;
  private LocalDateTime regDate;
  private LocalDateTime updateDate;
  private boolean is_complete;
}
