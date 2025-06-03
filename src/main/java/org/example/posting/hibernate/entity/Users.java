package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = "subscription")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(length = 100)
    private String name;

    @NotNull
    @Column(length = 100)
    private String surname;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Email
    @NotNull
    @Column(unique = true, length = 200)
    private String email;

    @NotNull
    @Column(name = "hd_password", length = 255)
    private String hdPassword;

    @NotNull
    @Past
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    @NotNull
    private Sex sex;

    private String photo;

    @NotNull
    @FutureOrPresent
    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Article> articles = new ArrayList<>();

    // Скорее всего часто не будет требоваться
//    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<LikeStats> likeStats = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ViewStats> viewStats = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        if (registrationDate == null){
            registrationDate = LocalDate.now();
        }
    }
}
