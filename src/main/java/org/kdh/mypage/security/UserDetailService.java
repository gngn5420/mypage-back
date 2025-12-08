package org.kdh.mypage.security;

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
public class UserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

//  @Override
//  public UserDetails loadUserByUsername(String username)
//      throws UsernameNotFoundException {
//
////    User user = userRepository.findByUsername(username);
//    User user = userRepository.findByUsername(username)
//        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//
//
//    if (user == null) {
//      throw new UsernameNotFoundException("User not found: " + username);
//    }
//
//    Set<GrantedAuthority> authorities =
//        Set.of(SecurityUtils.convertToAuthority(user.getRole().name()));
//
//    return UserPrincipal.builder()
//        .id(user.getId())
//        .username(user.getUsername())
//        .password(user.getPassword())
//        .authorities(authorities)
//        .build();
//  }

@Override
public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {

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
