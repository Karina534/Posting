package org.example.posting.hibernate.repository;

import org.example.posting.hibernate.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select count(n) from Notification n where n.user.userId = :userId")
    Long countByUserId(@Param("userId") Long id);
}
