package top.fallenangel.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.fallenangel.gateway.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);

    // void logout(String username);

    // void login(String username);
}
