package org.kdh.mypage.service;

import org.kdh.mypage.domain.Role;
import org.kdh.mypage.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    // 사용자 이름으로 사용자 찾기
    User findUserByUsername(String username);
    void changeRole(Role newRole, String usermame);
    void deleteUser(Long id);
    List<User> findAllUsers();

}
