package com.heroku.java.Controller;             

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.heroku.java.Model.Predict;
import com.heroku.java.Services.PredictServices;

@Controller
public class PredictController {
    private PredictServices predictServices;

    @Autowired
    public PredictController(PredictServices predictServices) {
        this.predictServices = predictServices;
    }

    @GetMapping("/predict-inventory")
    public String itemPredict(Model model, Predict predict) {
        try {
            List<Predict> predictList = predictServices.getPredict();
            model.addAttribute("Predict", predictList);
            return "admin/predict-inventory";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/dashboard-admin";
        }
    }

    @GetMapping("/new-predict")
    public String npredict() {
        return "admin/new-predict";
    }

    @PostMapping("/new-predict")
    public String predict() {
        return "admin/new-predict";
    }
}
