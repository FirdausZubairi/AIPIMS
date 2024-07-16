package com.heroku.java.Controller;            

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.heroku.java.Model.Item;
import com.heroku.java.Services.ItemServices;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Controller
public class ItemController {
    private ItemServices itemServices;

    @Autowired
    public ItemController(ItemServices itemServices) {
        this.itemServices = itemServices;
    }

    @GetMapping("/create-item")
    public String additem() {
        return "admin/create-item";
    }

    @PostMapping("/create-item")
    public String addItem(@ModelAttribute("item") Item item) {
        try {
            itemServices.addItem(item);
            return "redirect:/item";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            System.out.println("error");
        }
        return "admin/create-item";
    }

    @GetMapping("/item")
    public String Item(HttpSession session, Model model, Item item) {
        try {
            List<Item> itemList = itemServices.getAllItem();
            model.addAttribute("Item", itemList);
            return "admin/item";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/dashboard-admin";
        }
    }

    @GetMapping("/update-item")
    public String showUpdateItem(@RequestParam("iId") int itemId, Model model) {
        try {
            Item items = itemServices.getItemDetails(itemId);
            model.addAttribute("items", items);
            return "admin/update-item";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "admin/item";
        }
    }

    @PostMapping("/update-item")
    public String updateItem(HttpSession session, @RequestParam(name = "iId") int itemId,
                             @ModelAttribute("items") Item items, Model model) {
        try {
            itemServices.updateItem(itemId, items.getIname(), items.getIquantity(), items.getIcategory());
            return "redirect:/item";
        } catch (SQLException e) {
            System.out.println("message: " + e.getMessage());
            return "redirect:/dashboard-admin";
        }
    }

    @GetMapping("/delete-item")
    public String deleteItem(@RequestParam("iId") int itemId) {
        if (itemServices.deleteItem(itemId)) {
            return "redirect:/item";
        } else {
            return "item-not-found";
        }
    }

    @GetMapping("/item-staff")
    public String ItemStaff(HttpSession session, Model model, Item item) {
        try {
            List<Item> itemList = itemServices.getAllItem();
            model.addAttribute("Item", itemList);
            return "staff/item-staff";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "staff/dashboard-staff";
        }
    }
}
