package org.kdh.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.kdh.mypage.domain.Role;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.security.UserPrincipal;
import org.kdh.mypage.security.jwt.JwtProvider;
import org.kdh.mypage.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

  // 회원가입
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody User user) {
    userService.saveUser(user);
    return ResponseEntity.ok("REGISTERED");
  }

  // 로그인 → JWT 발급
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User loginUser) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginUser.getUsername(),
                loginUser.getPassword()
            )
        );

    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    String token = jwtProvider.generateToken(principal);
//    return ResponseEntity.ok(token);
    Map<String, Object> response = new HashMap<>();
    response.put("token", token);
    response.put("username", principal.getUsername());
    response.put("email", principal.getEmail());
    response.put("nickname", principal.getNickname());

    return ResponseEntity.ok(response);
  }

   // role 변경
  @PutMapping("/change/{role}")
  public ResponseEntity<Object> changeRole(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @PathVariable Role role) {
    userService.changeRole(role, userPrincipal.getUsername());
    return ResponseEntity.ok(true);
  }
}
