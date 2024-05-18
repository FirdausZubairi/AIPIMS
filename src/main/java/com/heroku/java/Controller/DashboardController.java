package com.heroku.java.Controller;             

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.heroku.java.Services.DashboardServices;
import org.springframework.ui.Model;
import java.sql.SQLException;

@Controller
public class DashboardController {
    private DashboardServices dashboardServices;

    @Autowired
    public DashboardController(DashboardServices dashboardServices) {
        this.dashboardServices = dashboardServices;
    }

    @GetMapping("/dashboard-admin")
    public String dashboardAdmin(Model model) {
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
    public String dashboardS(Model model) {
        try {
            int itemCount = dashboardServices.getItemCount();

            model.addAttribute("itemCount", itemCount);
        } catch (SQLException sqe) {
            sqe.printStackTrace(); // Handle or log the exception
            // You might want to add an error message to the model
            // model.addAttribute("error", "An error occurred while fetching dashboard data.");
        }
        return "staff/dashboard-staff";
    }

    @GetMapping("/about")
    public String about() {
        return "admin/about";
    }
}


