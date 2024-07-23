package com.heroku.java.Controller;             

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.heroku.java.Model.Users;
import com.heroku.java.Services.AccountServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class LoginController {
    private AccountServices accountServices;

    // @Autowired
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
    public String dashboard(HttpSession session, @ModelAttribute("Staff") Users users, RedirectAttributes redirectAttributes) {
        try {
            Users authenticatedUser = accountServices.authenticateUser(users.getUname(), users.getPword());

            if (authenticatedUser != null) {
                session.setAttribute("username", authenticatedUser.getUname());
                session.setAttribute("role", authenticatedUser.getRoles());
                session.setAttribute("staffid", authenticatedUser.getSid());
                System.out.println("user id is " + authenticatedUser.getSid());

                if ("admin".equals(authenticatedUser.getRoles())) {
                    return "redirect:/dashboard-admin?admin-loginsuccess=true";
                } else if ("staff".equals(authenticatedUser.getRoles())) {
                    session.setAttribute("staffId", authenticatedUser.getSid());
                    return "redirect:/dashboard-staff?staff-loginsuccess=true";
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password.");
            }
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred. Please try again.");
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(">>>>(" + session.getAttribute("staffid") + ") = " + session.getAttribute("username") + " logged out");
        
        // Invalidate the session
        session.invalidate();
        
        // Set headers to prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        return "redirect:/";
    }
}
