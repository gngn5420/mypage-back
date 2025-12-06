package org.kdh.mypage.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true, nullable = false, length=50)
  private String username;
  @Column(nullable = false, length=50)
  private String password;
  @Column(nullable = false)
  private String nickname;
  @Column(nullable = false)
  private String email;
  @Enumerated(EnumType.STRING) // Enum값 설정 Role.ADMIN / Role.USER
  @Column(nullable = false)
  private Role role;
  @Transient
  private String token;

}
