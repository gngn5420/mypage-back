package org.kdh.mypage.service;

import org.kdh.mypage.domain.User;

public interface AuthenticationService {
    public User singInAndReturnJWT(User signInRequest);
}
