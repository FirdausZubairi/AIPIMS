package com.heroku.java.Controller;             

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;

// import com.heroku.java.Model.Users;
// import com.heroku.java.Services.AccountServices;

// import jakarta.servlet.http.HttpSession;
// import java.sql.SQLException;

@Controller
public class PredictController {

    @GetMapping("/predict-inventory")
    public String predict() {
        return "admin/predict-inventory";
    }
}
