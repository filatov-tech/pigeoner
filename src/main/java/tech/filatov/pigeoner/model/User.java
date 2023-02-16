package tech.filatov.pigeoner.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractBaseEntity {

    private String email;

    private String password;

}
