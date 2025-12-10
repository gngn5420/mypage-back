package org.kdh.mypage.service;

import org.kdh.mypage.dto.HabitDTO;
import org.kdh.mypage.dto.HabitLogDTO;
import org.kdh.mypage.dto.HabitWithLogsDTO;

import java.util.List;

public interface HabitService {
  // 습관 추가
  HabitDTO addHabit(String username, HabitDTO habitDTO);

  // 습관 삭제
  void deleteHabit(Long habitId, String username);

  // 습관 목록 불러오기
  List<HabitDTO> getHabits(String username);

  // 해당 유저, 해당 날짜에 관한 습관 목록 불러오기
  List<HabitWithLogsDTO> getHabitsWithLogs(String username, List<String> dates);

  // 체크 된 것 true, 안 된 것 false 된 것 불러오기
  void toggleHabitCheck(Long habitId, String date, String username);

  // ✅ 토글 상태 체크 시 반환 (신규)
  HabitLogDTO toggleHabitCheckAndReturn(Long habitId, String date, String username);

  // 습관 수정하기
  HabitDTO updateHabitName(Long habitId, String newName, String username);

  // ✅ 이모지 수정 추가
  HabitDTO updateHabitEmoji(Long habitId, String newEmoji, String username);

}
