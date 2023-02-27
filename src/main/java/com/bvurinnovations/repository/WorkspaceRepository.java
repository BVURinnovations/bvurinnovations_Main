package com.bvurinnovations.repository;

import com.bvurinnovations.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, String> {
    @Query(value = "select * from workspace where id = :id and user_id = :userId ", nativeQuery = true)
    WorkspaceEntity findWorkspaceByIdAndUserId(@Param("id") String id, @Param("userId") String userId);
}
