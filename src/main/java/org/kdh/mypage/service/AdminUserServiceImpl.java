package org.kdh.mypage.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.domain.UserStatus;
import org.kdh.mypage.dto.AdminUserDTO;
import org.kdh.mypage.repository.HabitRepository;
import org.kdh.mypage.repository.TodoRepository;
import org.kdh.mypage.repository.UserRepository;
import org.kdh.mypage.repository.projection.HabitCountProjection;
import org.kdh.mypage.repository.projection.TodoCountProjection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
@Builder
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService, AdminUserCardService {

  private final UserRepository userRepository;
  private final TodoRepository todoRepository;
  private final HabitRepository habitRepository;

  // AdminUserService 구현 -----------------------------------------------------
  @Override
  @Transactional(readOnly = true)
  public List<User> findUsersByStatus(UserStatus status) {
    return userRepository.findByStatus(status);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findActiveAndSuspendedUsers() {
    return userRepository.findByStatusIn(
        List.of(UserStatus.ACTIVE, UserStatus.SUSPENDED)
    );
  }

  @Override
  public void updateStatus(Long id, UserStatus status) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found: " + id));

    user.setStatus(status);

    // ✅ 안전하게 명시 저장
    userRepository.save(user);
  }

  @Override
  public void softDeleteUser(Long id) {
    updateStatus(id, UserStatus.DELETED);
  }

  @Override
  public void restoreUser(Long id) {
    updateStatus(id, UserStatus.ACTIVE);
  }

  @Override
  public void hardDeleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("User not found: " + id);
    }
    userRepository.deleteById(id);
  }

  // AdminUserCardService---------------------------------------------------------------------
  @Override
  public List<AdminUserDTO> getUsers(String status) {

    UserStatus st = UserStatus.valueOf(status); // "ACTIVE", "DELETED"

    List<User> users = userRepository.findByStatus(st).stream()
        .filter(u -> !"ADMIN".equalsIgnoreCase(String.valueOf(u.getRole())))
        .toList();

    List<Long> userIds = users.stream().map(User::getId).toList();

    Map<Long, Long> todoMap = todoRepository.countByUserIds(userIds).stream()
        .collect(Collectors.toMap(
            TodoCountProjection::getUserId,
            TodoCountProjection::getCnt
        ));

    Map<Long, Long> habitMap = habitRepository.countByUserIds(userIds).stream()
        .collect(Collectors.toMap(
            HabitCountProjection::getUserId,
            HabitCountProjection::getCnt
        ));

    return users.stream()
        .map(u -> AdminUserDTO.builder()
            .id(u.getId())
            .username(u.getUsername())
            .email(u.getEmail())
            .nickname(u.getNickname())
            .role(String.valueOf(u.getRole()))
            .status(String.valueOf(u.getStatus()))
            .todoCount(todoMap.getOrDefault(u.getId(), 0L))
            .habitCount(habitMap.getOrDefault(u.getId(), 0L))
            .habit7dRate(0)
            .build())
        .toList();
  }
}
