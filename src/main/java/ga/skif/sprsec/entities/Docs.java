package ga.skif.sprsec.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @Column(name = "iddoc", unique = true, nullable = false)
    private Long iddoc;
    @Column(name = "datedoc", nullable = false)
    private Date datedoc;
    @Column(name = "titledoc", unique = true, nullable = false)
    private String titledoc;
    @Column(name = "textdoc", length=1023)
    private String textdoc;
    @ManyToOne
    private Account docowner;

}
