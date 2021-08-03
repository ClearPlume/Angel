package top.fallenangel.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.fallenangel.gateway.entity.RoleEntity;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    @Query(value = "select code\n" +
            "from t_role r\n" +
            "where exists(select role_id\n" +
            "             from t_role_api ra\n" +
            "             where exists(select id\n" +
            "                          from t_api\n" +
            "                          where uri = :uri\n" +
            "                            and id = ra.auth_id)\n" +
            "               and role_id = r.id)", nativeQuery = true)
    List<String> selectAllCodeByAuthUri(@Param("uri") String uri);

    @Modifying
    @Query(value = "delete from t_role_api where role_id = :id", nativeQuery = true)
    void deleteBatchById(@Param("id") Integer id);

    @Modifying
    @Query(value = "insert into t_role_api values (:authId, :roleId)", nativeQuery = true)
    void insert(@Param("roleId") Integer roleId, @Param("authId") Integer authId);

    @Query(value = "select id, code, name, description, enable from t_role where code = :code", nativeQuery = true)
    RoleEntity selectByCode(@Param("code") String code);
}
