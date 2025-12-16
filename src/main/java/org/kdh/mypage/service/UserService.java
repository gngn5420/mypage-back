package org.kdh.mypage.service;

import org.kdh.mypage.domain.Role;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.dto.RegisterRequestDTO;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    // 회원 가입
    void registerUser(RegisterRequestDTO dto);

    // 일반 유저 중심 설정
    User findUserByUsername(String username);

    void changeRole(Role newRole, String username);
    void deleteUser(Long id);
    List<User> findAllUsers();

  // ✅ 회원탈퇴: 현재 로그인한 사용자(username 기준) 탈퇴 처리
    void withdrawCurrentUser(String username);

    // 비밀번호 변경
    void changePassword(String username, String currentPassword, String newPassword);
}
