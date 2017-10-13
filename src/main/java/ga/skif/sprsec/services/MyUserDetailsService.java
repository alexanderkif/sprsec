package ga.skif.sprsec.services;

import ga.skif.sprsec.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    public MyUserDetailsService() {}

    @Override
    public UserDetails loadUserByUsername (String username) {
        Account account = accountRepository.findByEmail(username);
        if (account != null) {
            return new User(account.getEmail(), account.getPassword(),
                    true, true, true, true,
                    AuthorityUtils.createAuthorityList("USER"));
        }
        else{
            throw new UsernameNotFoundException("could not find the account '" + username + "'");
        }
    }
}

