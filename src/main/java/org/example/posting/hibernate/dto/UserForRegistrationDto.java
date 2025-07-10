package org.example.posting.hibernate.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.posting.hibernate.entity.Sex;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;

@Getter
public class UserForRegistrationDto {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String surname;

    @Size(max = 100)
    private String lastName;

    @Email
    @NotBlank
    @Size(max = 200)
    private String email;

    @Setter
    @NotBlank
    @Size(min = 6)
    private String hdPassword;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotNull
    private Sex sex;
    private String photo;

    @NotNull
    private boolean emailConfirmed;
}
