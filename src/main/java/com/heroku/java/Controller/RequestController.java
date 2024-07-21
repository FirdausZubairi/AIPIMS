package com.heroku.java.Controller;

import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.heroku.java.Model.Request;
import com.heroku.java.Model.RequestDetail;
import com.heroku.java.Services.RequestServices;
import jakarta.servlet.http.HttpSession;

@Controller
public class RequestController {
    private RequestServices requestServices;

    // @Autowired
    public RequestController(RequestServices requestServices) {
        this.requestServices = requestServices;
    }

    private boolean isSessionValid(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return username != null;
    }

    @GetMapping("/request")
    public String addRequest(HttpSession session, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        model.addAttribute("request", new Request());
        return "staff/request";
    }

    @PostMapping("/request")
    public String addReq(HttpSession session, @ModelAttribute("request") Request request, @RequestParam("itemCheckbox") List<String> checkboxValues) throws SQLException {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        System.out.println("proid: " + request.getProid());
        System.out.println("quantity: " + request.getReqQuantity());
        System.out.println("value = " + checkboxValues);

        requestServices.addReq(request, checkboxValues, session);

        return "redirect:/itemrequest";
    }
    
    @GetMapping("/approve-inventory")
    public String approveInventory(HttpSession session, Model model, Request request) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            List<Request> requestList = requestServices.getAllReq();
            model.addAttribute("Request", requestList);
            return "admin/approve-inventory";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/accept-approve")
    public String showApprove(HttpSession session, @RequestParam("rid") int requestId, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            List<RequestDetail> requestDetails = requestServices.getRequestDetails(requestId);
            model.addAttribute("requestDetails", requestDetails);
            return "admin/accept-approve";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }
     
    @PostMapping("/accept-approve")
    public String approveRequest(HttpSession session, @RequestParam("rid") int requestId, @RequestParam("proid") int projectId, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            List<RequestDetail> requestDetails = requestServices.getRequestDetails(requestId);
    
            for (RequestDetail detail : requestDetails) {
                int itemId = detail.getItemId();
                int projectQuantity = detail.getProjectQuantity();
                int reqQuantity = detail.getReqQuantity();
                int itemQuantity = requestServices.getItemQuantity(itemId); // Assuming you have a method to get the current item quantity

                System.out.println("Old Quantity" + itemQuantity);

                int newQuantity = itemQuantity - (projectQuantity * reqQuantity);
                System.out.println("New Quantity" + newQuantity);
                requestServices.updateItemQuantity(itemId, newQuantity);
            }
    
            // Update the request status to 'approved'
            requestServices.updateRequestStatus(requestId, "approved"); // Assuming you have a method to update request status
    
            model.addAttribute("message", "Request approved successfully.");
            return "redirect:/item"; // Redirect to the admin dashboard after approval
        } catch (SQLException e) {
            model.addAttribute("error", "Error approving request: " + e.getMessage());
            return "redirect:/";
        }
    }    
    
    @GetMapping("/reject-approve")
    public String showReject(HttpSession session, @RequestParam("rid") int requestId, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            List<RequestDetail> requestDetails = requestServices.getDetails(requestId);
            model.addAttribute("requestDetails", requestDetails);
            return "admin/reject-approve";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/reject-approve")
    public String rejectRequest(HttpSession session, @RequestParam("rid") int requestId, @RequestParam("proid") int projectId, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {    
            // Update the request status to 'approved'
            requestServices.updateRequestStatus(requestId, "rejected"); // Assuming you have a method to update request status
    
            model.addAttribute("message", "Request rejected successfully.");
            return "redirect:/approve-inventory"; // Redirect to the admin dashboard after approval
        } catch (SQLException e) {
            model.addAttribute("error", "Error approving request: " + e.getMessage());
            return "redirect:/";
        }
    }    

    @GetMapping("/itemrequest")
    public String itemRequest(HttpSession session, Model model, Request request) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        System.out.println("staff id: " + session.getAttribute("staffid"));
        try {
            List<Request> requestList = requestServices.getReq(session);
            model.addAttribute("Request", requestList);
            return "staff/itemrequest";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }
}
