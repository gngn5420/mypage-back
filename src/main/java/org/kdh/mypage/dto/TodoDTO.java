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
public class TodoDTO {
  private Long todoId;
  private String content;
  private String username;
  private LocalDateTime regDate;
  private LocalDateTime updateDate;
  private boolean complete;




}
