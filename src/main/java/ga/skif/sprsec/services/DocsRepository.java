package ga.skif.sprsec.services;

import ga.skif.sprsec.entities.Account;
import ga.skif.sprsec.entities.Docs;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DocsRepository extends CrudRepository<Docs, Long> {

    List<Docs> findByDocowner(Account docowner);
    List<Docs> findAll();
//    Docs findByTitledoc(String titledoc);

}
