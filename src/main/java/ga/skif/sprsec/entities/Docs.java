package ga.skif.sprsec.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "sprsecdocs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Docs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "iddoc", unique = true)
    private Long iddoc;
    @Column(name = "datedoc", nullable = false)
    private Date datedoc;
    @Column(name = "titledoc", unique = true)
    private String titledoc;
    @Column(name = "textdoc")
    private String textdoc;
    @ManyToOne
    private Account docowner;

}
