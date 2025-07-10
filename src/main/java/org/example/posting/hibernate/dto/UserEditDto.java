package org.example.posting.hibernate.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import org.example.posting.hibernate.entity.Sex;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;

@Getter
public class UserEditDto {
    @NotNull
    private Long userId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String surname;

    @Size(max = 100)
    private String lastName;

    private LocalDate birthDate;
    private Sex sex;
    private String photo;
}
