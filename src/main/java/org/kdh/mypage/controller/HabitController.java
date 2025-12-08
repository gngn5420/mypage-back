package org.kdh.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.kdh.mypage.dto.HabitDTO;
import org.kdh.mypage.dto.HabitWithLogsDTO;
import org.kdh.mypage.security.UserPrincipal;
import org.kdh.mypage.service.HabitService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habit")
@RequiredArgsConstructor
public class HabitController {

  private final HabitService habitService;

  /** 습관 생성 */
  @PostMapping("/add")
  public ResponseEntity<HabitDTO> addHabit(
      @AuthenticationPrincipal UserPrincipal user,
      @RequestBody HabitDTO dto
  ) {
    HabitDTO saved = habitService.addHabit(user.getUsername(), dto);
    return ResponseEntity.ok(saved);
  }

  /** 습관 삭제 */
  @DeleteMapping("/{habitId}")
  public ResponseEntity<?> deleteHabit(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable Long habitId
  ) {
    habitService.deleteHabit(habitId, user.getUsername());
    return ResponseEntity.ok("삭제 완료");
  }

  /** 모든 습관 조회 */
  @GetMapping("/list")
  public ResponseEntity<List<HabitDTO>> getHabits(
      @AuthenticationPrincipal UserPrincipal user
  ) {
    List<HabitDTO> habits = habitService.getHabits(user.getUsername());
    return ResponseEntity.ok(habits);
  }

  /** 날짜 리스트를 받아 습관 + 특정 기간 기록 조회 */
  @PostMapping("/logs")
  public ResponseEntity<List<HabitWithLogsDTO>> getHabitsWithLogs(
      @AuthenticationPrincipal UserPrincipal user,
      @RequestBody List<String> dates
  ) {
    List<HabitWithLogsDTO> result = habitService.getHabitsWithLogs(user.getUsername(), dates);
    return ResponseEntity.ok(result);
  }

  /** 습관 이름 수정 (인라인 수정 기능) */
  @PutMapping("/{habitId}/name")
  public ResponseEntity<HabitDTO> updateHabitName(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable Long habitId,
      @RequestBody HabitDTO dto
  ) {
    HabitDTO updated = habitService.updateHabitName(habitId, dto.getName(), user.getUsername());
    return ResponseEntity.ok(updated);
  }

}
