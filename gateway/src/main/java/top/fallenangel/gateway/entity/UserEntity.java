package top.fallenangel.gateway.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "t_user", schema = "sso")
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private RoleEntity role;

    @Column(name = "username", length = 16)
    private String username;

    @Column(name = "password", length = 60)
    private String password;

    @Column(name = "nickname", length = 16)
    private String nickname;

    @Column(name = "avatar", length = 64)
    private String avatar;

    @Column(name = "enable")
    private Boolean enable;
}
