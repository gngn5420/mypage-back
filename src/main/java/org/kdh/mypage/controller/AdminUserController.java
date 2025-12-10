package org.kdh.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.domain.UserStatus;
import org.kdh.mypage.dto.AdminUserDTO;
import org.kdh.mypage.service.AdminUserCardService;
import org.kdh.mypage.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

  private final AdminUserService adminUserService;
  private final AdminUserCardService adminUserCardService;

  /**
   * ✅ 관리자 탭 조회
   * - /api/admin/users?status=ACTIVE
   *   → ACTIVE + SUSPENDED 같이 반환(활성 탭에 정지 유저도 포함하려는 설계)
   *
   * - /api/admin/users?status=DELETED
   *   → DELETED만 반환
   */
  @GetMapping("/users")
  public ResponseEntity<List<User>> getUsers(@RequestParam String status) {

    UserStatus s = UserStatus.valueOf(status);

    if (s == UserStatus.ACTIVE) {
      return ResponseEntity.ok(adminUserService.findActiveAndSuspendedUsers());
    }

    return ResponseEntity.ok(adminUserService.findUsersByStatus(s));
  }

  /**
   * ✅ 상태 변경(정지/해제/삭제/복구를 한 엔드포인트로)
   * body 예:
   * { "status": "SUSPENDED" }
   * { "status": "ACTIVE" }
   * { "status": "DELETED" }
   */
  @PatchMapping("/users/{id}/status")
  public ResponseEntity<Void> updateStatus(
      @PathVariable Long id,
      @RequestBody UpdateStatusRequest request
  ) {
    UserStatus status = UserStatus.valueOf(request.getStatus());
    adminUserService.updateStatus(id, status);
    return ResponseEntity.ok().build();
  }

   // ✅ 소프트 삭제 전용 엔드포인트(원하면 사용)
   // 프론트에서 의미를 더 명확히 하고 싶을 때
  @PatchMapping("/users/{id}/soft-delete")
  public ResponseEntity<Void> softDelete(@PathVariable Long id) {
    adminUserService.softDeleteUser(id);
    return ResponseEntity.ok().build();
  }


   // ✅ 복구 전용 엔드포인트(원하면 사용)
  @PatchMapping("/users/{id}/restore")
  public ResponseEntity<Void> restore(@PathVariable Long id) {
    adminUserService.restoreUser(id);
    return ResponseEntity.ok().build();
  }


   //✅ 완전 삭제(하드 삭제)
   // - 삭제된 사용자 탭에서만 호출하도록 프론트에서 제한
  @DeleteMapping("/users/{id}/hard")
  public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
    adminUserService.hardDeleteUser(id);
    return ResponseEntity.ok().build();
  }

  // Request DTO
  public static class UpdateStatusRequest {
    private String status;

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }
  }

  // 관리자 페이지 회원 정보 카드 출력용
  @GetMapping(value = "/users", params = "mode=card")
  public ResponseEntity<List<AdminUserDTO>> getUsersCard(
      @RequestParam String status
  ) {
    return ResponseEntity.ok(adminUserCardService.getUsers(status));
  }

}
