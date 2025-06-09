package org.example.posting.hibernate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"subscription", "notifications", "articles"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
@EqualsAndHashCode(exclude = {"notifications", "articles"})
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
    @Column(name = "sex", columnDefinition = "VARCHAR(7) NOT NULL")
    @NotNull
    private Sex sex;

    @Column(columnDefinition = "TEXT")
    private String photo;

    @NotNull
    @FutureOrPresent
    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Article> articles = new HashSet<>();

    @Version
    private Long version;

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

    public void addNotification(Notification notification){
        notifications.add(notification);
        notification.setUser(this);
    }

    public void addArticles(Article article){
        articles.add(article);
        article.setUser(this);
    }
}
