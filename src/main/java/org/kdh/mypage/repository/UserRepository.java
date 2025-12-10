package org.kdh.mypage.repository;

import org.kdh.mypage.domain.Role;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  // 사용자 아이디 가져오기
  Optional<User> findByUsername(String username);

  // 사용자 역할 변경
  @Modifying
  @Query("update User set role=:role where username=:username")
  void updateUserRoles(@Param("username") String username, @Param("role") Role role);

  // Admin이 회원 상태 설정
  List<User> findByStatus(UserStatus status);
  List<User> findByStatusIn(List<UserStatus> statuses);

  // 회원가입 시 중복방지
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);

}
