package org.kdh.mypage.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
  private Long newsId;
  private String username;
  private String title;
  private String summary;
  private String link;
  private LocalDateTime regDate;
}
