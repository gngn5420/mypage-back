package org.kdh.mypage.security;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// 회원가입시 500에러 (서버) 어떤 문제인지 잡아줌.
@RestControllerAdvice
public class GlobalExceptionHandler {

  // ✅ 너가 서비스에서 던지는 그대로 잡아줌
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("message", e.getMessage()));
  }

  // ✅ exists 체크를 통과했는데 DB unique에서 터지는 케이스까지 보완
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("message", "이미 사용 중인 아이디 또는 이메일입니다."));
  }
}
