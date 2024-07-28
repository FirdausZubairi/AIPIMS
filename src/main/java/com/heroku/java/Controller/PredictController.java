package com.heroku.java.Controller;

import java.sql.SQLException;
import java.util.List;
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

    private boolean isSessionValid(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return username != null;
    }

    @GetMapping("/predict-inventory")
    public String itemPredict(HttpSession session, Model model, CaseBased casebased) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            List<CaseBased> predictList = predictServices.getPredict();
            model.addAttribute("Predict", predictList);
            return "admin/predict-inventory";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/search-predict")
    public String searchPredicts(HttpSession session, @RequestParam(name = "searchValue", required = false) String searchValue, Model model) throws SQLException {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }

        // Pass null or an empty string if no year is selected
        List<CaseBased> predictList = predictServices.getPredicts(searchValue);
        model.addAttribute("Predict", predictList);
        model.addAttribute("searchValue", searchValue);
        return "admin/predict-inventory";
    }


    @GetMapping("/new-predict")
    public String shownpredict(HttpSession session, @RequestParam("rid") int rId, @RequestParam("piid") int piId, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            Request request = predictServices.getRetrieveDetails(rId, piId);
            model.addAttribute("requests", request);
            System.out.println("Req ID: " + rId);
            return "admin/new-predict";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/new-predict")
    public String addPredict(HttpSession session, @ModelAttribute("requests") CaseBased casebased, Request request) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            predictServices.addPredict(casebased);
            return "redirect:/predict-inventory?retain-success=true";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            System.out.println("error");
        }
        return "redirect:/";
    }

    @GetMapping("/prediction-request")
    public String predict(HttpSession session, Model model, Request request, CaseBased casebased) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            List<Request> requestList = predictServices.getReq();
            model.addAttribute("Request", requestList);
            return "admin/prediction-request";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            System.out.println("error");
        }
        return "redirect:/";
    }

    @GetMapping("/revised-predict")
    public String revisedpredict(HttpSession session, @RequestParam("cbrid") int cbrId, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            CaseBased casebased = predictServices.getRevisedDetails(cbrId);
            model.addAttribute("requests", casebased);
            System.out.println("CBR ID: " + cbrId);
            return "admin/revised-predict";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/restock")
    public String restockItem(HttpSession session, @RequestParam("itemid") Integer itemId,
                              @RequestParam("predictedQuan") Integer predictedQuan,
                              @RequestParam("iquantity") Integer iQuantity,
                              RedirectAttributes redirectAttributes) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            Integer currentIquantity = iQuantity;
            Integer newIquantity = currentIquantity + predictedQuan;
            predictServices.restockItem(newIquantity, itemId);

            redirectAttributes.addFlashAttribute("successMessage", "Item restocked successfully.");
            return "redirect:/item?restock-success=true";
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to restock item: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/revised-predict")
    public String updateItem(HttpSession session, @RequestParam(name = "cbrID") int cbrId,
                             @ModelAttribute("requests") CaseBased casebased, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            predictServices.RevisedPredict(cbrId, casebased.getReqID(), casebased.getPiid(), casebased.getPredictedQuan(), casebased.getYears());
            return "redirect:/predict-inventory?revised-success=true";
        } catch (SQLException e) {
            System.out.println("message: " + e.getMessage());
            return "redirect:/";
        }
    }
}
