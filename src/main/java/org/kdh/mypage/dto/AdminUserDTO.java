package org.kdh.mypage.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDTO {
  private Long id;
  private String username;
  private String nickname;
  private String email;
  private String role;
  private String status;

  // 관리자 페이지 회원 카드 출력용 지표
  private long todoCount;
  private long habitCount;
  private int habit7dRate; // 0~100
}
