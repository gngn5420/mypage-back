package org.kdh.mypage.service;

import lombok.RequiredArgsConstructor;
import org.kdh.mypage.domain.Habit;
import org.kdh.mypage.domain.HabitLog;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.dto.HabitDTO;
import org.kdh.mypage.dto.HabitWithLogsDTO;
import org.kdh.mypage.repository.HabitRepository;
import org.kdh.mypage.repository.HabitLogRepository;
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

//  @Override
//  public HabitDTO addHabit(String username, HabitDTO habitDTO) {
//
//    User user = userRepository.findByUsername(username);
//
//    Habit habit = Habit.builder()
//        .name(habitDTO.getName())
//        .emoji(habitDTO.getEmoji())
//        .user(user)
//        .build();
//
//    Habit saved = habitRepository.save(habit);
//
//    return HabitDTO.builder()
//        .habitId(saved.getHabitId())
//        .name(saved.getName())
//        .emoji(saved.getEmoji())
//        .build();
//  }
@Override
public HabitDTO addHabit(String username, HabitDTO habitDTO) {

  User user = userRepository.findByUsername(username)
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


  @Override
  public void deleteHabit(Long habitId, String username) {

    Habit habit = habitRepository.findById(habitId)
        .orElseThrow(() -> new RuntimeException("Habit not found"));

    if (!habit.getUser().getUsername().equals(username)) {
      throw new RuntimeException("권한이 없습니다.");
    }

    habitRepository.delete(habit);
  }

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
  // 습관 이름, 이모지 업데이트
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


}
