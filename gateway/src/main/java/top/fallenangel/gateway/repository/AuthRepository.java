package top.fallenangel.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.fallenangel.gateway.entity.AuthEntity;

import java.util.List;

public interface AuthRepository extends JpaRepository<AuthEntity, Integer> {
    @Query(value = "select id, name, parent, uri\n" +
            "from t_auth a\n" +
            "where exists(select role_id from t_role_auth ra where ra.role_id = :roleId and id = ra.auth_id)", nativeQuery = true)
    List<AuthEntity> selectAllByRoleId(@Param("roleId") Integer roleId);
}