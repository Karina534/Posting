package org.example.posting.hibernate.repository;

import org.example.posting.hibernate.entity.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @EntityGraph(attributePaths = {"subscription"})
    Optional<Users> findByUserId(Long id);

    Optional<Users> findByEmail(String email);
}
