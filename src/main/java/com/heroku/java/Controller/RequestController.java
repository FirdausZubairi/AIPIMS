package com.heroku.java.Controller;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import com.heroku.java.Model.Item;
import com.heroku.java.Model.Request;
import com.heroku.java.Model.RequestDetail;
import com.heroku.java.Services.RequestServices;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Controller
public class RequestController {
    private RequestServices requestServices;

    // @Autowired
    public RequestController(RequestServices requestServices) {
        this.requestServices = requestServices;
    }

    @GetMapping("/request")
    public String addRequest(Model model) {
        model.addAttribute("request", new Request());
        return "staff/request";
    }

    @PostMapping("/request")
    public String addReq(HttpSession session, @ModelAttribute("request") Request request, @RequestParam("itemCheckbox") List<String> checkboxValues) throws SQLException {
        System.out.println("proid: " + request.getProid());
        System.out.println("quantity: " + request.getReqQuantity());
        System.out.println("value = " + checkboxValues);

        requestServices.addReq(request, checkboxValues, session);

        return "redirect:/itemrequest";
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
            List<RequestDetail> requestDetails = requestServices.getRequestDetails(requestId);
            model.addAttribute("requestDetails", requestDetails);
            return "admin/accept-approve";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/approve-inventory";
        }
    }
     
    @PostMapping("/accept-approve")
    public String approveRequest(@RequestParam("rid") int requestId, @RequestParam("proid") int projectId, Model model) {
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
            return "admin/accept-approve";
        }
    }    
    
    //Reject
    @GetMapping("/reject-approve")
    public String showReject(@RequestParam("rid") int requestId, Model model) {
        try {
            Request requests = requestServices.getDetails(requestId);
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
        System.out.println("staff id: " + session.getAttribute("staffid"));
        try {
            List<Request> requestList = requestServices.getReq(session);
            model.addAttribute("Request", requestList);
            return "staff/itemrequest";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "staff/dashboard-staff";
        }
    }

}
