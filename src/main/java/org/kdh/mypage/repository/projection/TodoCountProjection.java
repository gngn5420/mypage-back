package org.kdh.mypage.repository.projection;

// 쿼리 전용 인터페이스 (DTO 클래스X)
// db 조회 결과 형태만 정의
// 구현 클래스 없음
public interface TodoCountProjection {
  Long getUserId();
  Long getCnt();
}
