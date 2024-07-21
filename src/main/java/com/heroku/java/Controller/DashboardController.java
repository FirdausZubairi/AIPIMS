package com.heroku.java.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.heroku.java.Services.DashboardServices;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class DashboardController {
    private DashboardServices dashboardServices;

    // @Autowired
    public DashboardController(DashboardServices dashboardServices) {
        this.dashboardServices = dashboardServices;
    }

    private boolean isSessionValid(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return username != null;
    }

    @GetMapping("/dashboard-admin")
    public String dashboardAdmin(HttpSession session, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/"; // or an appropriate error page
        }

        try {
            int itemCount = dashboardServices.getItemCount();
            int staffCount = dashboardServices.getStaffCount();
            int requestCount = dashboardServices.getRequestCount();
            int approveCount = dashboardServices.getApproveCount();

            model.addAttribute("itemCount", itemCount);
            model.addAttribute("accountCount", staffCount);
            model.addAttribute("requestCount", requestCount);
            model.addAttribute("approveCount", approveCount);
        } catch (SQLException sqe) {
            sqe.printStackTrace(); // Handle or log the exception
            // You might want to add an error message to the model
            // model.addAttribute("error", "An error occurred while fetching dashboard data.");
        }

        return "admin/dashboard-admin";
    }

    @GetMapping("/dashboard-staff")
    public String dashboardS(HttpSession session, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/"; // or an appropriate error page
        }

        int staffId = (int) session.getAttribute("staffId"); // Assuming you have stored staffId in session

        try {
            int itemCount = dashboardServices.getItemCount();
            int requestStaffCount = dashboardServices.getRequestStaffCount(staffId);

            model.addAttribute("itemCount", itemCount);
            model.addAttribute("requestCount", requestStaffCount);
        } catch (SQLException sqe) {
            sqe.printStackTrace(); // Handle or log the exception
            // You might want to add an error message to the model
            // model.addAttribute("error", "An error occurred while fetching dashboard data.");
        }
        return "staff/dashboard-staff";
    }


    @GetMapping("/about")
    public String about(HttpSession session) {
        if (!isSessionValid(session)) {
            return "redirect:/"; // or an appropriate error page
        }
        return "admin/about";
    }
}
