package org.example.posting.hibernate.mapper;

import org.example.posting.hibernate.dto.ShortArticleReadDto;
import org.example.posting.hibernate.entity.Article;
import org.springframework.stereotype.Component;

@Component
public class ShortArticleReadMapper implements Mapper<Article, ShortArticleReadDto>{
    @Override
    public ShortArticleReadDto mapFrom(Article obj) {
        return new ShortArticleReadDto(
                obj.getArticleId(),
                obj.getUser().getUserId(),
                obj.getTitle(),
                obj.getPublishedDate(),
                obj.getCategory().getTitle()
        );
    }
}
