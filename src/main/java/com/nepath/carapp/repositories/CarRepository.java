package com.nepath.carapp.repositories;

import com.nepath.carapp.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Modifying
    @Query("UPDATE Car SET user.id = :userId WHERE id = :carId")
    void changeCarOwner(@Param(value = "carId") Long carId, @Param(value = "userId") Long userId);
}
