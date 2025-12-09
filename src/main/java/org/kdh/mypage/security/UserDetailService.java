package org.kdh.mypage.security;

import lombok.extern.log4j.Log4j2;
import org.kdh.mypage.domain.User;
import org.kdh.mypage.repository.UserRepository;
import org.kdh.mypage.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Log4j2
public class UserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

@Override
public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    log.info("Loading USer"+username);

  User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

  Set<GrantedAuthority> authorities =
      Set.of(SecurityUtils.convertToAuthority(user.getRole().name()));

  return UserPrincipal.builder()
      .id(user.getId())
      .username(user.getUsername())
      .password(user.getPassword())
      .email(user.getEmail())
      .nickname(user.getNickname())
      .authorities(authorities)
      .build();
}

}
