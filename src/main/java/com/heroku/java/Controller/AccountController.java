package com.heroku.java.Controller;             

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AccountController(AccountServices accountServices) {
        this.accountServices = accountServices;
    }

    @GetMapping("/add-account")
    public String addAccount() {
        return "admin/add-account";
    }
    @PostMapping("/add-account")
    public String addAcc(@ModelAttribute("user") Users user) {
        try {
            accountServices.addAccount(user);
            return "redirect:/account";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            System.out.println("error");
        }
        return "admin/add-account";
    }

    @GetMapping("/account")
    public String Account(HttpSession session, Model model, Users User) {
        try {
            List<Users> userList = accountServices.getAllUsers();
            model.addAttribute("Users", userList);
            return "admin/account";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/dashboard-admin";
        }
    }

    @GetMapping("/update-account")
    public String showUpdateAccount(@RequestParam("sid") int staffId, Model model) {
        try {
            Users user = accountServices.getAccountDetails(staffId);
            model.addAttribute("user", user);
            return "admin/update-account";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/dashboard-admin";
        }
    }

    @PostMapping("/update-account")
    public String updateAccount(HttpSession session, @RequestParam(name = "sid") int staffId,
                                @ModelAttribute("user") Users users, Model model) {
        try {
            accountServices.updateAccount(staffId, users.getName(), users.getUname(), users.getPword(), users.getRoles());
            return "redirect:/account";
        } catch (SQLException e) {
            System.out.println("message: " + e.getMessage());
            return "redirect:/dashboard-admin";
        }
    }

    @GetMapping("/delete-account")
    public String deleteAccount(@ModelAttribute("user") Users users, @RequestParam("sid") int staffId, Model model) {
        if (accountServices.deleteAccount(staffId)) {
            return "redirect:/account";
        } else {
            return "account-not-found";
        }
    }
    
}
