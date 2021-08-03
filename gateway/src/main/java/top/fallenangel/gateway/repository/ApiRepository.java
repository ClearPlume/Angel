package top.fallenangel.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.fallenangel.gateway.entity.ApiEntity;

import java.util.List;

public interface ApiRepository extends JpaRepository<ApiEntity, Integer> {
    @Query(value = "select id, name, parent, uri\n" +
            "from t_api a\n" +
            "where exists(select role_id from t_role_api ra where ra.role_id = :roleId and id = ra.auth_id)", nativeQuery = true)
    List<ApiEntity> selectAllByRoleId(@Param("roleId") Integer roleId);
}