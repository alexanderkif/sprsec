package ga.skif.sprsec.services;


import ga.skif.sprsec.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByEmail(String email);

}
