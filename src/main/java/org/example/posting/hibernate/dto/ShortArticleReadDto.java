package org.example.posting.hibernate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.posting.hibernate.entity.Category;
import org.example.posting.hibernate.entity.Users;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ShortArticleReadDto {
    @NotNull
    private Long articleId;

    @NotNull
    @NotBlank
    private Long userId;

    @NotBlank
    @Size(max = 400)
    private String title;

    @NotBlank
    private LocalDate publishedDate;

    @NotNull
    private String categoryTitle;
}
