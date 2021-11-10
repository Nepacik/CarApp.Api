package com.nepath.carapp.repositories;

import com.nepath.carapp.models.User;
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

}
