package top.fallenangel.gateway.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "t_auth", schema = "sso")
public class AuthEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 16, nullable = false)
    private String name;

    @Column(name = "uri", length = 32, nullable = false)
    private String uri;

    @OneToOne
    @JoinColumn(name = "parent", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private AuthEntity parent;
}
