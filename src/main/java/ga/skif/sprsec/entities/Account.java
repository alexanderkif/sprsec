package ga.skif.sprsec.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sprsecusers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "uid", unique = true)
    private Long uid;
    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @NotNull
    @Column(name = "password")
    private String password;
    @Column(name = "enabled")
    private boolean enabled;
}
