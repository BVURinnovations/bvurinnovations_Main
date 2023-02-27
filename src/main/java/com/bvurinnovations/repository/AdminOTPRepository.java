package com.bvurinnovations.repository;

import com.bvurinnovations.entity.AdminOTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminOTPRepository extends JpaRepository<AdminOTPEntity, String> {
    @Query(nativeQuery = true, value = "select * from admin_otp where otp = :otp and user_id = :userId")
    AdminOTPEntity findByOTPAndUserId(@Param("otp") Integer otp, @Param("userId") String userId);

    @Query(nativeQuery = true, value = "select * from admin_otp where otp = :otp and user_id = :userId")
    AdminOTPEntity findByUserId(@Param("userId") String userId);
}
