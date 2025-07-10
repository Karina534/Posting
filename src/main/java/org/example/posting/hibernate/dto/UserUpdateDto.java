package org.example.posting.hibernate.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.example.posting.hibernate.entity.Sex;

import java.time.LocalDate;

@Getter
public class UserUpdateDto {
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String surname;

    @Size(max = 100)
    private String lastName;

    @Past
    private LocalDate birthDate;

    private Sex sex;
    private String photo;
}