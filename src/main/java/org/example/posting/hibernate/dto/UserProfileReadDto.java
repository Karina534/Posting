package org.example.posting.hibernate.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.posting.hibernate.entity.Sex;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class UserProfileReadDto {
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

    @Email
    @NotBlank
    @Size(max = 200)
    private String email;

    @NotBlank
    @Past
    private LocalDate birthDate;

    @NotBlank
    private Sex sex;

    private String photo;

    @NotNull
    @NotBlank
    private SubscriptionReadDto subscription;

    private Long notificationCount;

    private List<ShortArticleReadDto> articles;
}
