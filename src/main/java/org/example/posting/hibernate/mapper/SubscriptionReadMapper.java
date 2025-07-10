package org.example.posting.hibernate.mapper;

import org.example.posting.hibernate.dto.SubscriptionReadDto;
import org.example.posting.hibernate.entity.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionReadMapper implements Mapper<Subscription, SubscriptionReadDto>{

    @Override
    public SubscriptionReadDto mapFrom(Subscription obj) {
        return new SubscriptionReadDto(
                obj.getSubscriptionId(),
                obj.getSubscriptionType().getTitle(),
                obj.getStartDate(),
                obj.getEndDate()
        );
    }
}
