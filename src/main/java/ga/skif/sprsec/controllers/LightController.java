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
import org.springframework.web.bind.annotation.SessionAttributes;

import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@SessionAttributes(value = "light")
public class LightController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DocsRepository docsRepository;

    @Autowired
    PasswordEncoder encoder;

    private String titl;
    final Integer docsOnPage = 3;



    public LightController() {
    }

    @RequestMapping(value = "/light")
    public String home(Model model, @ModelAttribute("light") String light) {
        model.addAttribute("light", "on");
        return "redirect:/light/index?page=1";
    }


    @RequestMapping(value = "/light/index")
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
        titl = "Index";
        model.addAttribute("titl", titl);
        model.addAttribute("docs", docs);
        model.addAttribute("pages", pages);
        model.addAttribute("page", page);
        return "light/index";
    }

    @RequestMapping("/light/login")
    public String login(Model model) {
        titl = "Login";
        model.addAttribute("titl", titl);
        return "light/login";
    }

    @RequestMapping("/light/edit")
    public String edit(Model model) {
        titl = "Edit";
        model.addAttribute("titl", titl);
        return "light/edit";
    }

    @RequestMapping("/light/adddoc")
    public String adddoc(Model model, Principal principal,
                         @ModelAttribute("titleDoc") String titleDoc,
                         @ModelAttribute("textDoc") String textDoc) {
        try {
            Account account = accountRepository.findByEmail(principal.getName());
            List<Docs> docs = docsRepository.findByDocowner(account);
            Docs doc = docs.stream()
                    .filter(d -> d.getTitledoc().equals(titleDoc))
                    .findFirst()
                    .orElse(new Docs());
            if (Objects.equals(textDoc, "delete")){
                docsRepository.delete(doc);
                titl = "Deleted";
            }
            else {
                doc.setTitledoc(titleDoc);
                doc.setTextdoc(textDoc);
                doc.setDatedoc(new Date());
                doc.setDocowner(account);
                docsRepository.save(doc);
                titl = "Saved";
            }
        } catch (Exception e){
            titl = "Not saved";
        }
        model.addAttribute("titl", titl);
        return "light/edit";
    }

    @RequestMapping("/light/list")
    public String list(Model model, Principal principal) {
        Account account = accountRepository.findByEmail(principal.getName());
        List<Docs> docs = docsRepository.findByDocowner(account);
        titl = "List";
        model.addAttribute("titl", titl);
        model.addAttribute("docs", docs);
        return "light/list";
    }

    @RequestMapping("/light/register")
    public String register(Model model) {
        titl = "Register";
        model.addAttribute("titl", titl);
        return "light/register";
    }

    @RequestMapping("/light/adduser")
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
            titl = "Added";
        } else {
            titl = "Not added";
        }
        model.addAttribute("titl", titl);
        return "light/login";
    }
}
