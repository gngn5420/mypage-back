package org.kdh.mypage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 관리자한테도 안 보이게
  @Column(nullable = false, length = 255)
  private String password;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String email;

  // 로그인한 사용자 권한 설정
  @Enumerated(EnumType.STRING) // Enum값 설정 Role.ADMIN / Role.USER
  @Column(nullable = false)
  private Role role;

  @Transient
  private String token;

  // 하나의 User가 여러 Todo를 가질 수 있도록 설정
  @JsonIgnore //Api 요청시 응답 JSON에서만 안 보이도록 함
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Todo> todos;  // 사용자가 작성한 Todo 목록

  // 관리자가 유저 정보 관리하기 위한 필드 (정지, 삭제 등)
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private UserStatus status = UserStatus.ACTIVE;




}
