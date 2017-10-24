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
import org.springframework.web.bind.annotation.RequestParam;

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
        return "redirect:/index?page=1";
    }


    @RequestMapping(value = "/index")
    public String index(Model model, @RequestParam("page") String p) {
        Integer page;
        if (Objects.equals(p, "")){
            page=1;
        } else{
            page = Integer.valueOf(p); }
        List<Docs> docs2 = docsRepository.findAll();
        Long pages = Math.round(Math.ceil(1.0 * docs2.size() / docsOnPage));
        List docs = docs2.stream()
                .sorted(Comparator.comparing(Docs::getDatedoc).reversed())
                .skip((page-1)*docsOnPage)
                .limit(docsOnPage)
                .collect(Collectors.toList());
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

    @RequestMapping("/edit")
    public String edit(Model model) {
        li = "edit";
        titl = "Edit";
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "edit";
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
                li = "edit";
                titl = "Deleted";
            }
            else {
                doc.setTitledoc(titleDoc);
                doc.setTextdoc(textDoc);
                doc.setDatedoc(new Date());
                doc.setDocowner(account);
                docsRepository.save(doc);
                li = "edit";
                titl = "Saved";
            }
        } catch (Exception e){
            li = "edit";
            titl = "Not saved";
        }
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "edit";
    }

    @RequestMapping("/list")
    public String list(Model model, Principal principal) {
        Account account = accountRepository.findByEmail(principal.getName());
        List<Docs> docs = docsRepository.findByDocowner(account);
        li = "list";
        titl = "List";
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        model.addAttribute("docs", docs);
        return "list";
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
    public String adduser(Model model,
                          @ModelAttribute("username") String email, @ModelAttribute("password") String password) {
        if (accountRepository.findByEmail(email) == null
                && !Objects.equals(email, "") && !Objects.equals(password, "")) {
            accountRepository.save(Account.builder()
                    .email(email)
                    .password(encoder.encode(password))
                    .enabled(true)
                    .build()
            );
            li = "login";
            titl = "Added";
        } else {
            li = "login";
            titl = "Not added";
        }
        model.addAttribute("links", li);
        model.addAttribute("titl", titl);
        return "login";
    }
}
