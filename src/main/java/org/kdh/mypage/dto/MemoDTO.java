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
public class MemoDTO {
  private String username;
  private Long memoId;
  private String title;
  private String content;
  private String source_url;
  private LocalDateTime regDate;
}
