package org.kdh.mypage.service;

import org.kdh.mypage.dto.AdminUserDTO;

import java.util.List;

public interface AdminUserCardService {

  // 관리자 페이지 프로필 카드 출력용
  // ✅ 탭별 목록 (ACTIVE / DELETED)
  List<AdminUserDTO> getUsers(String status);
}
