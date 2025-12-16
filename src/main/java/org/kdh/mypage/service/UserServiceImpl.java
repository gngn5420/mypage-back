package org.kdh.mypage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kdh.mypage.domain.Role;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.domain.UserStatus;
import org.kdh.mypage.dto.RegisterRequestDTO;
import org.kdh.mypage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  // UserService 영역 -----------------------------------------------
  @Override
  public User saveUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // ✅ role이 비어있을 때만 기본 USER
    if (user.getRole() == null) {
      user.setRole(Role.USER);
    }

    // ✅ status가 비어있을 때만 기본 ACTIVE
    if (user.getStatus() == null) {
      user.setStatus(UserStatus.ACTIVE);
    }

    return userRepository.save(user);
  }

  // 회원가입 설정 ------------------------------------------
  @Override
  public void registerUser(RegisterRequestDTO dto) {
    if (userRepository.existsByUsername(dto.getUsername())) {
      throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
    }

    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    }

    if (userRepository.existsByNickname(dto.getNickname())) {
      throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
    }

    User user = new User();
    user.setUsername(dto.getUsername());
    user.setNickname(dto.getNickname());
    user.setEmail(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));

    user.setRole(Role.USER);
    user.setStatus(UserStatus.ACTIVE);

    userRepository.save(user);
  }

  // 회원 찾기 ------------------------
  @Override
  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found: " + username));
  }

  // Role 역할 변경---------------------
  @Override
  public void changeRole(Role newRole, String username) {
    userRepository.updateUserRoles(username, newRole);
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  // ✅ 회원탈퇴 로직
  @Transactional
  public void withdrawCurrentUser(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found: " + username));

    user.setStatus(UserStatus.DELETED);

    userRepository.save(user);
  }

  // 비밀번호 변경
  @Override
  public void changePassword(String username, String currentPassword, String newPassword) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found: " + username));

    // ✅ 현재 비밀번호 체크
    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      // 프론트에서 구분하기 위해 의도적으로 이 문자열 사용
      throw new IllegalArgumentException("BAD_PASSWORD");
    }

    // ✅ 새 비밀번호로 변경
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

}
