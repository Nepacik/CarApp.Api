package com.nepath.carapp.repositories;

import com.nepath.carapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByNick(String nick);

    boolean existsUserByEmail(String email);

    boolean existsUserByNick(String nick);

    @Query(value = "SELECT * FROM users ORDER BY email", nativeQuery = true)
    Page<User> findAllUsersSortByEmail(Pageable pageable);
}
