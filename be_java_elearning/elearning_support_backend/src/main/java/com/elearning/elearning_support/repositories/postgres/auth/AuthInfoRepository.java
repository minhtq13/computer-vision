package com.elearning.elearning_support.repositories.postgres.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.entities.auth.AuthInfo;

@Repository
public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {

    Optional<AuthInfo> findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<AuthInfo> findByRefreshToken(String refreshToken);

}
