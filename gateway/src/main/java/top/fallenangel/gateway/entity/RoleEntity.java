package top.fallenangel.gateway.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "t_role", schema = "sso")
public class RoleEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", length = 16)
    private String code;

    @Column(name = "name", length = 8)
    private String name;

    @Column(name = "description", length = 15)
    private String description;

    @Column(name = "enable")
    private Boolean enable;
}
