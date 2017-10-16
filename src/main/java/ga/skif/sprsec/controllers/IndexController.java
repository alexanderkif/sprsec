package ga.skif.sprsec.controllers;

import ga.skif.sprsec.entities.Account;
import ga.skif.sprsec.services.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PasswordEncoder encoder;

    private String titl;
    private String li;

    public IndexController() {}

    @RequestMapping(value = "/")
    public String home(Model model) {
        li = "index";
        titl = "Index";
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        li = "login";
        titl = "Login";
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "login";
    }

    @RequestMapping("/about")
    public String out(Model model) {
        li = "about";
        titl = "About";
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "about";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        li = "register";
        titl = "Register";
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "register";
    }

    @RequestMapping("/adduser")
    public String adduser(Model model, @ModelAttribute("username") String email, @ModelAttribute("password") String password) {
        if (accountRepository.findByEmail(email)==null) {
            accountRepository.save(Account.builder()
                    .email(email)
                    .password(encoder.encode(password))
                    .enabled(true)
                    .build()
            );
            li = "index";
            titl = "Added";
        }
        else {
            li = "index";
            titl = "Not added";
        }
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "index";
    }

}
