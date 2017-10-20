package ga.skif.sprsec.controllers;

import ga.skif.sprsec.entities.Account;
import ga.skif.sprsec.entities.Docs;
import ga.skif.sprsec.services.AccountRepository;
import ga.skif.sprsec.services.DocsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DocsRepository docsRepository;

    @Autowired
    PasswordEncoder encoder;

    private String titl;
    private String li;
    final Integer docsOnPage = 3;

    public IndexController() {
    }

    @RequestMapping(value = "/")
    public String home(Model model) {
        Integer page = 0; // Integer.valueOf(request.getParameter("page"));
//        if (page==null){ page=0;}
        List<Docs> docs2 = docsRepository.findAll();
        Long pages = Math.round(Math.ceil(docs2.size() / docsOnPage));
        List docs = docs2.stream().sorted(Comparator.comparing(Docs::getDatedoc).reversed()).skip(page*docsOnPage).limit(3).collect(Collectors.toList());
        li = "index";
        titl = "Index";
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        model.addAttribute("docs", docs);
        model.addAttribute("pages", pages);
        model.addAttribute("page", page);
        return "index";
    }

    @RequestMapping(value = "/index")
    public String home(Model model, @ModelAttribute("page") String p) {
        Integer page;
        if (Objects.equals(p, "")){
            page=0;
        } else{
            page = Integer.valueOf(p); }
        List<Docs> docs2 = docsRepository.findAll();
        Long pages = Math.round(Math.ceil(docs2.size() / docsOnPage));
        List docs = docs2.stream().sorted(Comparator.comparing(Docs::getDatedoc).reversed()).skip(page*docsOnPage).limit(3).collect(Collectors.toList());
        li = "index";
        titl = "Index";
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        model.addAttribute("docs", docs);
        model.addAttribute("pages", pages);
        model.addAttribute("page", page);
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

    @RequestMapping("/secret")
    public String secret(Model model) {
        li = "secret";
        titl = "Secret";
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "secret";
    }

    @RequestMapping("/adddoc")
    public String adddoc(Model model, @ModelAttribute("titleDoc") String titleDoc,
                         @ModelAttribute("textDoc") String textDoc, Principal principal) {
        try {
            Account account = accountRepository.findByEmail(principal.getName());
            List<Docs> docs = docsRepository.findByDocowner(account);
            Docs doc = docs.stream()
                    .filter(d -> d.getTitledoc().equals(titleDoc))
                    .findFirst()
                    .orElse(new Docs());
            if (Objects.equals(textDoc, "delete")){
                docsRepository.delete(doc);
                li = "secret";
                titl = "Deleted";
            }
            else {
                doc.setTitledoc(titleDoc);
                doc.setTextdoc(textDoc);
                doc.setDatedoc(new Date());
                doc.setDocowner(account);
                docsRepository.save(doc);
                li = "secret";
                titl = "Saved";
            }
        } catch (Exception e){
            li = "secret";
            titl = "Not saved";
        }
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "secret";
    }

    @RequestMapping("/secret2")
    public String secret2(Model model, Principal principal) {
        Account account = accountRepository.findByEmail(principal.getName());
        List<Docs> docs = docsRepository.findByDocowner(account);
        li = "secret2";
        titl = "Secret2";
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        model.addAttribute("docs", docs);
        return "secret2";
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
        if (accountRepository.findByEmail(email) == null) {
            accountRepository.save(Account.builder()
                    .email(email)
                    .password(encoder.encode(password))
                    .enabled(true)
                    .build()
            );
            li = "index";
            titl = "Added";
        } else {
            li = "index";
            titl = "Not added";
        }
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "index";
    }
}
