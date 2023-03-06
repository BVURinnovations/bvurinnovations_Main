package com.bvurinnovations.repository;

import com.bvurinnovations.entity.RollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RollRepository extends JpaRepository<RollEntity, String> {
    @Query(nativeQuery = true, value = "select * from rolls where created_by = :createdBy and status = 'ACTIVE' ")
    List<RollEntity> getRollsByUserId(String createdBy);
}
