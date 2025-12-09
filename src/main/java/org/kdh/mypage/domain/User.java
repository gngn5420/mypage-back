package org.kdh.mypage.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @Column(nullable = false, length = 255)
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

  // 하나의 User가 여러 Todo를 가질 수 있도록 설정
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Todo> todos;  // 사용자가 작성한 Todo 목록

}
