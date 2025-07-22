package org.example.posting.hibernate.mapper;

import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.dto.UserForRegistrationDto;
import org.example.posting.hibernate.entity.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserCreateForRegistrationMapper implements Mapper<UserForRegistrationDto, Users>{
    private final PasswordEncoder passwordEncoder;
    @Override
    public Users mapFrom(UserForRegistrationDto obj) {
        return Users.builder()
                .name(obj.getName())
                .surname(obj.getSurname())
                .lastName(obj.getLastName())
                .email(obj.getEmail())
                .hdPassword(passwordEncoder.encode(obj.getHdPassword()))
                .birthDate(obj.getBirthDate())
                .sex(obj.getSex())
                .photo(obj.getPhoto())
                .registrationDate(LocalDate.now())
                .emailConfirmed(false)
                .build();
    }
}
