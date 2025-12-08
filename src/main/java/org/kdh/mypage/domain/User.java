package org.kdh.mypage.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
//@ToString(onlyExplicitlyIncluded = true)
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length=50)
  private String username;

  @Column(nullable = false, length=255)
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
