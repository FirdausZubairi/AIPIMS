package com.heroku.java.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// import com.heroku.java.Model.Item;
import com.heroku.java.Model.Request;
import com.heroku.java.Services.RequestServices;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Controller
public class RequestController {
    private RequestServices requestServices;

    @Autowired
    public RequestController(RequestServices requestServices) {
        this.requestServices = requestServices;
    }

    @GetMapping("/request")
    public String addRequest() {
        return "staff/request";
    }

    @PostMapping("/request")
    public String addReq(HttpSession session, @ModelAttribute("request") Request request) {
        System.out.println("proid: " + request.getProid());
        System.out.println("quantity: " + request.getReqQuantity());
        try {
            requestServices.addReq(request);
            return "redirect:/itemrequest";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            System.out.println("error");
        }
        return "staff/dashboard-staff";
    }
    
    @GetMapping("/approve-inventory")
    public String approveInventory(HttpSession session, Model model, Request request) {
        try {
            List<Request> requestList = requestServices.getAllReq();
            model.addAttribute("Request", requestList);
            return "admin/approve-inventory";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/dashboard-admin";
        }
    }

    //Approve
    @GetMapping("/accept-approve")
    public String showApprove(@RequestParam("rid") int requestId, Model model) {
        try {
            Request requests = requestServices.getrequestDetails(requestId);
            model.addAttribute("requests", requests);
            return "admin/accept-approve";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/approve-inventory";
        }
    }

    @PostMapping("/accept-approve")
    public String approve(HttpSession session, @RequestParam(name = "rid") int requestId, @ModelAttribute("requests") Request request, Model model) {
        try {
            System.out.println("proid: " + request.getProid());
            System.out.println("reqQuantity: " + request.getReqQuantity());

            requestServices.approveInventory(requestId, request.getProid(), request.getReqQuantity(), request.getRstatus());
            return "redirect:/approve-inventory";
        } catch (SQLException e) {
            System.out.println("message: " + e.getMessage());
            return "redirect:/dashboard-admin";
        }
    }
    
    //Reject
    @GetMapping("/reject-approve")
    public String showReject(@RequestParam("rid") int requestId, Model model) {
        try {
            Request requests = requestServices.getrequestDetails(requestId);
            model.addAttribute("requests", requests);
            return "admin/reject-approve";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/approve-inventory";
        }
    }

    @PostMapping("/reject-approve")
    public String reject(HttpSession session, @RequestParam(name = "rid") int requestId, @ModelAttribute("requests") Request request, Model model) {
        try {
            requestServices.rejectInventory(requestId, request.getProid(), request.getReqQuantity(), request.getRstatus());
            return "redirect:/approve-inventory";
        } catch (SQLException e) {
            System.out.println("message: " + e.getMessage());
            return "redirect:/dashboard-admin";
        }
    }

    //staff view status
    @GetMapping("/itemrequest")
    public String itemRequest(HttpSession session, Model model, Request request) {
        try {
            List<Request> requestList = requestServices.getReq();
            model.addAttribute("Request", requestList);
            return "staff/itemrequest";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "staff/dashboard-staff";
        }
    }
}
