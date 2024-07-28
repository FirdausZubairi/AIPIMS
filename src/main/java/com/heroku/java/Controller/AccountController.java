package com.heroku.java.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Map;

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
    public String account(HttpSession session, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            List<Users> userList = accountServices.getAllUsers();
            // Mask passwords
            for (Users user : userList) {
                user.setPword("*****"); // Mask the password
            }
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
            return "redirect:/account?update-accountsuccess=false";
        }
    }

    // @GetMapping("/delete-account")
    // public String deleteAccount(HttpSession session, @ModelAttribute("user") Users users, @RequestParam("sid") int staffId, Model model) {
    //     if (!isSessionValid(session)) {
    //         return "redirect:/";
    //     }
    //     if (accountServices.deleteAccount(staffId)) {
    //         return "redirect:/account?delete-accountsuccess=true";
    //     } else {
    //         model.addAttribute("deleteMessage", "You cannot delete the account. The account has made a request.");
    //         return "redirect:/account";
    //     }
    // }

    // @GetMapping("/delete-account")
    // public String deleteAccount(HttpSession session, @ModelAttribute("user") Users users, @RequestParam("sid") int staffId, Model model) {
    //     if (!isSessionValid(session)) {
    //         return "redirect:/";
    //     }
    //     boolean isDeleted = accountServices.deleteAccount(staffId);
    //     if (isDeleted) {
    //         return "redirect:/account?delete-accountsuccess=true";
    //     } else {
    //         model.addAttribute("deleteMessage", "You cannot delete the account. The account has made a request.");
    //         return "redirect:/account"; // Assuming "account" is the name of the view where the message should be displayed
    //     }
    // }

    @GetMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(HttpSession session, @RequestParam("sid") int staffId) {
        if (!isSessionValid(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        if (!accountServices.canDeleteAccount(staffId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You cannot delete the account. The account has made a request.");
        }
        boolean isDeleted = accountServices.deleteAccount(staffId);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Account deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the account.");
        }
    }
}
