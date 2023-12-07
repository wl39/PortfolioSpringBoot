package com.wl39.portfolio.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
    User findUser(@Param("username") String username, @Param("password") byte[] password);

    @Query("SELECT u.password FROM User u WHERE u.username = ?1")
    Optional<byte[]> findPasswordByUsername(String username);
}
