package org.kdh.mypage.repository;

import org.kdh.mypage.domain.Role;
import org.kdh.mypage.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Modifying  // 데이터블 insert, update, delete 할 때 반드시 필요함.
    @Query("update User set role=:role where username=:username")
    void updateUserRoles(@Param("username") String username, @Param("role") Role role);
}

// 업데이트, 삭제할 때 반드시 @Transactional 안에서 실행되어야 한다.
