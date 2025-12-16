package org.kdh.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.kdh.mypage.domain.Role;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.domain.UserStatus;
import org.kdh.mypage.dto.ChangePasswordRequest;
import org.kdh.mypage.dto.RegisterRequestDTO;
import org.kdh.mypage.security.UserPrincipal;
import org.kdh.mypage.security.jwt.JwtProvider;
import org.kdh.mypage.service.AdminUserService;
import org.kdh.mypage.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;
  private final AdminUserService adminUserService;

  // 회원가입 ----------------------------------------------------------
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
    userService.registerUser(registerRequestDTO);
    return ResponseEntity.ok("REGISTERED");
  }

  // 로그인 ------------------------------------------------------------
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User loginUser) {

    // 1) 아이디 존재 여부 먼저 확인 (UserService 사용)
    User user;
    try {
      user = userService.findUserByUsername(loginUser.getUsername());
    } catch (RuntimeException e) {
      // ❗ 없는 아이디
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of(
              "reason", "NO_SUCH_USER"
          ));
    }

    // 2) 탈퇴 / 정지 상태 체크
    if (user.getStatus() == UserStatus.DELETED) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(Map.of(
              "reason", "DELETED",
              "message", "탈퇴한 계정입니다."
          ));
    }

    if (user.getStatus() == UserStatus.SUSPENDED) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(Map.of(
              "reason", "SUSPENDED",
              "message", "정지된 계정입니다."
          ));
    }

    // 3) 비밀번호 검증 (기존 authenticate 로직 + 비밀번호 틀림 분기)
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginUser.getUsername(),
                  loginUser.getPassword()
              )
          );

      UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
      String token = jwtProvider.generateToken(principal);

      Map<String, Object> response = new HashMap<>();
      response.put("token", token);
      response.put("username", principal.getUsername());
      response.put("email", principal.getEmail());
      response.put("nickname", principal.getNickname());
      response.put("role", principal.getRole());

      return ResponseEntity.ok(response);

    } catch (BadCredentialsException ex) {
      // ❗ 아이디는 있는데 비밀번호가 틀린 경우
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of(
              "reason", "BAD_PASSWORD"
          ));
    }
  }

  // 로그인된 사용자 정보 가져오기 ------------------------------------
  @GetMapping("/me")
  public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    Map<String, Object> response = new HashMap<>();
    response.put("username", userPrincipal.getUsername());
    response.put("email", userPrincipal.getEmail());
    response.put("nickname", userPrincipal.getNickname());
    response.put("role", userPrincipal.getRole());

    return ResponseEntity.ok(response);
  }

  // role 변경 --------------------------------------------------------
  @PutMapping("/change/{role}")
  public ResponseEntity<Object> changeRole(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @PathVariable Role role) {
    userService.changeRole(role, userPrincipal.getUsername());
    return ResponseEntity.ok(true);
  }

  // 회원이 스스로 회원 탈퇴 (소프트 삭제) ------------------------------
  @PostMapping("/withdraw")
  public ResponseEntity<Void> withdraw(@AuthenticationPrincipal UserPrincipal user) {

    // ❗ 지금은 withdrawCurrentUser 에서 status=DELETED + withdrawAt 세팅한다고 가정
    userService.withdrawCurrentUser(user.getUsername());

    // 만약 관리자용 softDeleteUser(id)만 쓰고 싶다면 위 한 줄 대신:
    // adminUserService.softDeleteUser(user.getId());

    return ResponseEntity.ok().build();
  }

  // 비밀번호 변경 -----------------------------------------------------
  @PostMapping("/password")
  public ResponseEntity<?> changePassword(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody ChangePasswordRequest request
  ) {
    try {
      userService.changePassword(
          userPrincipal.getUsername(),
          request.getCurrentPassword(),
          request.getNewPassword()
      );
      return ResponseEntity.ok(Map.of("result", "OK"));
    } catch (IllegalArgumentException ex) {

      if ("BAD_PASSWORD".equals(ex.getMessage())) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("reason", "BAD_PASSWORD"));
      }

      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(Map.of("reason", "BAD_REQUEST"));
    }
  }
}
