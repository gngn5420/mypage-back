package org.kdh.mypage.security;

import org.kdh.mypage.domain.User;
import org.kdh.mypage.repository.UserRepository;
import org.kdh.mypage.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException(username);
        }
        Set<GrantedAuthority> authorities = Set.of(SecurityUtils
                .convertToAuthority(user.getRole().name()));  // 한 사람이 권한을 여러개 가지기도 함.

        return org.kdh.mypage.security.UserPrincipal.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .user(user)
                .build();
    }
}
