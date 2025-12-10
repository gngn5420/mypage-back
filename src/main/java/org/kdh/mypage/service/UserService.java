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

}
