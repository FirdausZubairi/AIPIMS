package com.heroku.java.Controller;             

import java.sql.SQLException;
import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.heroku.java.Model.CaseBased;
import com.heroku.java.Model.Request;
import com.heroku.java.Services.PredictServices;

import jakarta.servlet.http.HttpSession;


@Controller
public class PredictController {
    private PredictServices predictServices;
    

    // @Autowired
    public PredictController(PredictServices predictServices) {
        this.predictServices = predictServices;
    }

    @GetMapping("/predict-inventory")
    public String itemPredict(Model model, CaseBased casebased) {
        try {
            List<CaseBased> predictList = predictServices.getPredict();
            model.addAttribute("Predict", predictList);
            return "admin/predict-inventory";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/dashboard-admin";
        }
    }

    @GetMapping("/search-predict")
    public String searchPredicts(@RequestParam(name = "searchValue", required = false) String searchValue, Model model) throws SQLException {
        List<CaseBased> predictList = predictServices.getPredicts(searchValue);
        model.addAttribute("Predict", predictList);
        model.addAttribute("searchValue", searchValue);
        return "admin/predict-inventory";
    }
        
    @GetMapping("/new-predict")
    public String shownpredict(@RequestParam("rid") int rId, @RequestParam("piid") int piId, Model model) {
        try {
                Request request = predictServices.getRetrieveDetails(rId,piId);
                model.addAttribute("requests", request);
                System.out.println("Req ID: " + rId);
                return "admin/new-predict";
            } catch (SQLException e) {
                System.out.println("message : " + e.getMessage());
                return "admin/dashboard-admin";
            }
    }

    @PostMapping("/new-predict")
    public String addPredict(@ModelAttribute("requests") CaseBased casebased, Request request) {
        try {
            predictServices.addPredict(casebased);
            return "redirect:/predict-inventory";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            System.out.println("error");
        }
        return "admin/dashboard-admin";
    }

    @GetMapping("/prediction-request")
    public String predict(Model model, Request request, CaseBased casebased) {
        try {
            List<Request> requestList = predictServices.getReq();
            model.addAttribute("Request", requestList);
            return "admin/prediction-request";
        } catch (SQLException e) {
        System.out.println("message : " + e.getMessage());
        System.out.println("error");
        }
        return "admin/dashboard-admin";
    }

    @GetMapping("/revised-predict")
    public String revisedpredict(@RequestParam("cbrid") int cbrId, Model model) {
        try {
            CaseBased casebased = predictServices.getRevisedDetails(cbrId);
            model.addAttribute("requests", casebased);
            System.out.println("CBR ID: " + cbrId);
            return "admin/revised-predict";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/dashboard-admin";
        }
    }
    
    @PostMapping("/restock")
    public String restockItem(@RequestParam("itemid") Integer itemId,
                              @RequestParam("predictedQuan") Integer predictedQuan,
                              @RequestParam("iquantity") Integer iQuantity,
                              RedirectAttributes redirectAttributes) {
        try {
            Integer currentIquantity = iQuantity;
            Integer newIquantity = currentIquantity + predictedQuan;
            predictServices.restockItem(newIquantity, itemId);
    
            redirectAttributes.addFlashAttribute("successMessage", "Item restocked successfully.");
            return "redirect:/item";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to restock item: " + e.getMessage());
            return "redirect:/dashboard-admin";
        }
    }
    

    @PostMapping("/revised-predict")
    public String updateItem(HttpSession session, @RequestParam(name = "cbrID") int cbrId,
                             @ModelAttribute("requests") CaseBased casebased, Model model) {
        try {
            predictServices.RevisedPredict(cbrId, casebased.getReqID(), casebased.getPiid(), casebased.getPredictedQuan(), casebased.getYears() );
            return "redirect:/predict-inventory";
        } catch (SQLException e) {
            System.out.println("message: " + e.getMessage());
            return "redirect:/dashboard-admin";
        }
    }

    @GetMapping("/create-project")
    public String addproject() {
        return "admin/create-project";
    }
    
}
