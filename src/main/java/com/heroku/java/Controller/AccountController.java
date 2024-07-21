package com.heroku.java.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.heroku.java.Model.Users;
import com.heroku.java.Services.AccountServices;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Controller
public class AccountController {
    private AccountServices accountServices;

    // @Autowired
    public AccountController(AccountServices accountServices) {
        this.accountServices = accountServices;
    }

    private boolean isSessionValid(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return username != null;
    }



    @GetMapping("/add-account")
    public String addAccount(HttpSession session) {
        if (!isSessionValid(session)) {
            return "redirect:/login"; // or an appropriate error page
        }
        return "admin/add-account";
    }

    @PostMapping("/add-account")
    public String addAcc(HttpSession session, @ModelAttribute("user") Users user, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            accountServices.addAccount(user);
            return "redirect:/account?add-accountsuccess=true";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            System.out.println("error");
            return "redirect:/";
        }
    }

    @GetMapping("/account")
    public String Account(HttpSession session, Model model, Users User) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            List<Users> userList = accountServices.getAllUsers();
            model.addAttribute("Users", userList);
            return "admin/account";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/update-account")
    public String showUpdateAccount(HttpSession session, @RequestParam("sid") int staffId, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            Users user = accountServices.getAccountDetails(staffId);
            model.addAttribute("user", user);
            return "admin/update-account";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/update-account")
    public String updateAccount(HttpSession session, @RequestParam(name = "sid") int staffId,
                                @ModelAttribute("user") Users users, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            accountServices.updateAccount(staffId, users.getName(), users.getUname(), users.getPword(), users.getRoles());
            return "redirect:/account?update-accountsuccess=true";
        } catch (SQLException e) {
            System.out.println("message: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/delete-account")
    public String deleteAccount(HttpSession session, @ModelAttribute("user") Users users, @RequestParam("sid") int staffId, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        if (accountServices.deleteAccount(staffId)) {
            return "redirect:/account?delete-accountsuccess=true";
        } else {
            return "redirect:/";
        }
    }
}
