package com.heroku.java.Controller;             

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.Model.CaseBased;
import com.heroku.java.Model.Predict;
import com.heroku.java.Model.Request;
import com.heroku.java.Services.PredictServices;


@Controller
public class PredictController {
    private PredictServices predictServices;

    @Autowired
    public PredictController(PredictServices predictServices) {
        this.predictServices = predictServices;
    }

    @GetMapping("/predict-inventory")
    public String itemPredict(Model model, CaseBased cbr) {
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
        List<Predict> predictList = predictServices.getPredicts(searchValue);
        model.addAttribute("Predict", predictList);
        model.addAttribute("searchValue", searchValue);
        return "admin/predict-inventory";
    }

    @GetMapping("/new-predict")
    public String npredict() {
        return "admin/new-predict";
    }

    @PostMapping("/new-predict")
    public String addPredict(@ModelAttribute("predict") Predict predict) {
        try {
            predictServices.addPredict(predict);
            return "redirect:/predict-inventory";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            System.out.println("error");
        }
        return "admin/dashboard-admin";
    }

    @GetMapping("/prediction-request")
    public String predict(Model model, Request request) {
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
    public String revisedpredict() {
        return "admin/revised-predict";
    }

    @GetMapping("/create-project")
    public String addproject() {
        return "admin/create-project";
    }
}
