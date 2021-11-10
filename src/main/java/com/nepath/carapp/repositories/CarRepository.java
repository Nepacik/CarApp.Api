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

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Car c WHERE c.id = :carId and c.user.id = :userId")
    boolean existsCarWithOwnerId(@Param(value = "carId") Long carId, @Param(value = "userId") Long userId);
}
