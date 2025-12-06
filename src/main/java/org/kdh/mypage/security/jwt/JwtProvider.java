package org.kdh.mypage.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.kdh.mypage.security.UserPrincipal;
import org.springframework.security.core.Authentication;

public interface JwtProvider {
    //토근 생성 (로그인 처리될 때)
    String generateToken(UserPrincipal userPrincipal);
    //인증정보 얻기
    Authentication getAuthentication(HttpServletRequest request);
    // 토근 유효성 검증
    boolean isTokenValid(HttpServletRequest request);
}
