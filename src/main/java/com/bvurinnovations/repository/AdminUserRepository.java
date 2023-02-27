package com.bvurinnovations.repository;

import com.bvurinnovations.entity.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUserEntity, String> {
    @Query(nativeQuery = true, value = "select * from research.admin_user;")
    AdminUserEntity getAllUser();

    @Query(value = "select * from bvur.admin_user where mobile = :mobile ", nativeQuery = true)
    AdminUserEntity findAdminUserByMobile(@Param("mobile") String mobile);

    @Query(value = "select * from admin_user where id = :id ", nativeQuery = true)
    AdminUserEntity findAdminUserById(@Param("id") String id);
}
