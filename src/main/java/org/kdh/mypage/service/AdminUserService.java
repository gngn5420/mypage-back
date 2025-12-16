package org.kdh.mypage.service;

import org.kdh.mypage.domain.User;
import org.kdh.mypage.domain.UserStatus;

import java.util.List;

public interface AdminUserService {

  // 상태별 목록
  List<User> findUsersByStatus(UserStatus status);

  // ACTIVE 탭에서 정지(SUSPENDED)도 같이 보고 싶을 때
  List<User> findActiveAndSuspendedUsers();

  // 상태변경
  void updateStatus(Long id, UserStatus status);

  // 소프트 삭제 (상태만 delete) , 회원탈퇴 + 관리자 삭제 공용으로 사용
  void softDeleteUser(Long id);

  // 복구
  void restoreUser(Long id);

  // 완전 삭제
  void hardDeleteUser(Long id);


}
