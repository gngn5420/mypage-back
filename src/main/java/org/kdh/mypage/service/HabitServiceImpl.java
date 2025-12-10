package org.kdh.mypage.service;

import lombok.RequiredArgsConstructor;
import org.kdh.mypage.domain.Habit;
import org.kdh.mypage.domain.HabitLog;
import org.kdh.mypage.dto.HabitDTO;
import org.kdh.mypage.dto.HabitLogDTO;
import org.kdh.mypage.dto.HabitWithLogsDTO;
import org.kdh.mypage.repository.HabitLogRepository;
import org.kdh.mypage.repository.HabitRepository;
import org.kdh.mypage.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

  private final HabitRepository habitRepository;
  private final HabitLogRepository habitLogRepository;
  private final UserRepository userRepository;

  // 습관 추가
  @Override
  public HabitDTO addHabit(String username, HabitDTO habitDTO) {
    var user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Habit habit = Habit.builder()
        .name(habitDTO.getName())
        .emoji(habitDTO.getEmoji())
        .user(user)
        .build();

    Habit saved = habitRepository.save(habit);

    return HabitDTO.builder()
        .habitId(saved.getHabitId())
        .name(saved.getName())
        .emoji(saved.getEmoji())
        .build();
  }

  // 습관 삭제
  @Override
  public void deleteHabit(Long habitId, String username) {
    Habit habit = habitRepository.findById(habitId)
        .orElseThrow(() -> new RuntimeException("Habit not found"));

    if (!habit.getUser().getUsername().equals(username)) {
      throw new RuntimeException("권한이 없습니다.");
    }
    // ✅ 1) 로그 먼저 삭제
    habitLogRepository.deleteByHabit(habit);
    // ✅ 2) 습관 삭제
    habitRepository.delete(habit);
  }

  // 습관 가져오기
  @Override
  public List<HabitDTO> getHabits(String username) {
    return habitRepository.findByUserUsername(username).stream()
        .map(h -> HabitDTO.builder()
            .habitId(h.getHabitId())
            .name(h.getName())
            .emoji(h.getEmoji())
            .build())
        .collect(Collectors.toList());
  }

  // 습관 로그 가져오기
  @Override
  public List<HabitWithLogsDTO> getHabitsWithLogs(String username, List<String> dates) {
    List<Habit> habits = habitRepository.findByUserUsername(username);

    return habits.stream().map(habit -> {
      Map<String, Boolean> logsMap = new LinkedHashMap<>();

      for (String date : dates) {
        HabitLog log = habitLogRepository.findByHabitAndDate(habit, date).orElse(null);
        logsMap.put(date, log != null && log.isChecked());
      }

      return HabitWithLogsDTO.builder()
          .habitId(habit.getHabitId())
          .name(habit.getName())
          .emoji(habit.getEmoji())
          .logs(logsMap)
          .build();
    }).collect(Collectors.toList());
  }

  // 토글 체크
  @Override
  public void toggleHabitCheck(Long habitId, String date, String username) {
    Habit habit = habitRepository.findById(habitId)
        .orElseThrow(() -> new RuntimeException("Habit not found"));

    if (!habit.getUser().getUsername().equals(username)) {
      throw new RuntimeException("권한이 없습니다.");
    }

    HabitLog log = habitLogRepository.findByHabitAndDate(habit, date).orElse(null);

    if (log == null) {
      habitLogRepository.save(
          HabitLog.builder()
              .habit(habit)
              .date(date)
              .checked(true)
              .build()
      );
    } else {
      log.setChecked(!log.isChecked());
      habitLogRepository.save(log);
    }
  }

  // 토글 후 결과 DTO 반환 (화면에 db 결과 돌려줌)
  @Override
  public HabitLogDTO toggleHabitCheckAndReturn(Long habitId, String date, String username) {

    Habit habit = habitRepository.findById(habitId)
        .orElseThrow(() -> new RuntimeException("Habit not found"));

    if (!habit.getUser().getUsername().equals(username)) {
      throw new RuntimeException("권한이 없습니다.");
    }

    HabitLog log = habitLogRepository.findByHabitAndDate(habit, date).orElse(null);

    HabitLog saved;
    if (log == null) {
      saved = habitLogRepository.save(
          HabitLog.builder()
              .habit(habit)
              .date(date)
              .checked(true)
              .build()
      );
    } else {
      log.setChecked(!log.isChecked());
      saved = habitLogRepository.save(log);
    }

    return HabitLogDTO.builder()
        .logId(saved.getLogId())
        .habitId(habit.getHabitId())
        .date(saved.getDate())
        .checked(saved.isChecked())
        .build();
  }

  // 습관 이름 업데이트
  @Override
  public HabitDTO updateHabitName(Long habitId, String newName, String username) {
    Habit habit = habitRepository.findById(habitId)
        .orElseThrow(() -> new RuntimeException("Habit not found"));

    if (!habit.getUser().getUsername().equals(username))
      throw new RuntimeException("권한 없음");

    habit.setName(newName);
    Habit saved = habitRepository.save(habit);

    return HabitDTO.builder()
        .habitId(saved.getHabitId())
        .name(saved.getName())
        .emoji(saved.getEmoji())
        .build();
  }
  // 이모지 수정
  @Override
  public HabitDTO updateHabitEmoji(Long habitId, String newEmoji, String username) {
    Habit habit = habitRepository.findById(habitId)
        .orElseThrow(() -> new RuntimeException("Habit not found"));

    if (!habit.getUser().getUsername().equals(username))
      throw new RuntimeException("권한 없음");

    habit.setEmoji(newEmoji);
    Habit saved = habitRepository.save(habit);

    return HabitDTO.builder()
        .habitId(saved.getHabitId())
        .name(saved.getName())
        .emoji(saved.getEmoji())
        .build();
  }
}
