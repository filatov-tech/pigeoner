package tech.filatov.pigeoner.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractBaseEntity {

    private String email;

    private String password;

    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne
    private Keeper keeper;

}
