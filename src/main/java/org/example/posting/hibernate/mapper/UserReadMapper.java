package org.example.posting.hibernate.mapper;

import lombok.RequiredArgsConstructor;
import org.example.posting.hibernate.dto.UserProfileReadDto;
import org.example.posting.hibernate.entity.Article;
import org.example.posting.hibernate.entity.Users;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserReadMapper{
    private final SubscriptionReadMapper subscriptionReadMapper;
    private final ShortArticleReadMapper shortArticleReadMapper;

    public UserProfileReadDto mapFrom(Users user, Long notificationCount, List<Article> article) {
        return new UserProfileReadDto(
                user.getUserId(),
                user.getName(),
                user.getSurname(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getSex(),
                user.getPhoto(),
                subscriptionReadMapper.mapFrom(user.getSubscription()),
                notificationCount,
                article.stream().map(shortArticleReadMapper::mapFrom).collect(Collectors.toList())
        );
    }

//    private int safeSize(Set<?> set){
//        return set != null ? set.size() : 0;
//    }
}
