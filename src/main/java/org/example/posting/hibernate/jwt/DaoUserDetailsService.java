package org.example.posting.hibernate.jwt;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.entity.Users;
import org.example.posting.hibernate.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DaoUserDetailsService implements UserDetailsService {
    /*
    Класс для превращения пользователя в UserDetails для провайдера
     */

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        return User.builder()
                .username(user.getEmail())
                .password(user.getHdPassword())
                .authorities("USER")
                .build();
    }
}
