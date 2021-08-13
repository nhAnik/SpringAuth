package com.nhanik.springauth.repository;

import com.nhanik.springauth.model.SecurityToken;
import com.nhanik.springauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface SecurityTokenRepository extends JpaRepository<SecurityToken, Long> {

    Optional<SecurityToken> findByToken(String token);

    Optional<SecurityToken> findByUser(User user);

    @Modifying
    @Query("delete from SecurityToken t where t.expiryDate <= ?1")
    void deleteAllExpiredToken(Instant now);
}
