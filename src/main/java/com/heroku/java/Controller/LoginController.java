package com.heroku.java.Controller;             

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.heroku.java.Model.Users;
import com.heroku.java.Services.AccountServices;

import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class LoginController {
    private AccountServices accountServices;

    @Autowired
    public LoginController(AccountServices accountServices) {
        this.accountServices = accountServices;
    }

    @GetMapping("/")
    public String login(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "admin/dashboard-admin";
        } else {
            return "login";
        }
    }

    @PostMapping("/login")
    public String dashboard(HttpSession session, @ModelAttribute("Staff") Users users) {
        try {
            Users authenticatedUser = accountServices.authenticateUser(users.getUname(), users.getPword());

            if (authenticatedUser != null) {
                session.setAttribute("username", authenticatedUser.getUname());
                session.setAttribute("role", authenticatedUser.getRoles());
                session.setAttribute("staffid", authenticatedUser.getSid());
                System.out.println("user id is " + authenticatedUser.getSid());

                if ("admin".equals(authenticatedUser.getRoles())) {
                    return "redirect:/dashboard-admin";
                } else if ("staff".equals(authenticatedUser.getRoles())) {
                    return "redirect:/dashboard-staff";
                }
            }
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
        }
        return "/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println("succesfully logout");
        return "redirect:/";
    }

}
