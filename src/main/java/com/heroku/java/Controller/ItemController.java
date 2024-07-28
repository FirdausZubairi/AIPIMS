package com.heroku.java.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // @Autowired
    public ItemController(ItemServices itemServices) {
        this.itemServices = itemServices;
    }

    private boolean isSessionValid(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return username != null;
    }

    @GetMapping("/create-item")
    public String addItem(HttpSession session) {
        if (!isSessionValid(session)) {
            return "redirect:/"; // or an appropriate error page
        }
        return "admin/create-item";
    }

    @PostMapping("/create-item")
    public String addItem(HttpSession session, @ModelAttribute("item") Item item) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            itemServices.addItem(item);
            return "redirect:/item?create-itemsuccess=true";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            System.out.println("error");
            return "redirect:/";
        }
    }

    @GetMapping("/item")
    public String item(HttpSession session, Model model, Item item) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            List<Item> itemList = itemServices.getAllItem();
            model.addAttribute("Item", itemList);
            return "admin/item";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/update-item")
    public String showUpdateItem(HttpSession session, @RequestParam("iId") int itemId, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            Item items = itemServices.getItemDetails(itemId);
            model.addAttribute("items", items);
            return "admin/update-item";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/update-item")
    public String updateItem(HttpSession session, @RequestParam(name = "iId") int itemId,
                             @ModelAttribute("items") Item items, Model model) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            itemServices.updateItem(itemId, items.getIname(), items.getIquantity(), items.getIcategory());
            return "redirect:/item?update-itemsuccess=true";
        } catch (SQLException e) {
            System.out.println("message: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/delete-item")
    public ResponseEntity<String> deleteItem(HttpSession session, @RequestParam("iId") int itemId) {
        if (!isSessionValid(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        if (!itemServices.canDeleteItem(itemId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You cannot delete the item. The item has already in request.");
        }
        boolean isDeleted = itemServices.deleteItem(itemId);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Item deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the item.");
        }
    }

    @GetMapping("/item-staff")
    public String itemStaff(HttpSession session, Model model, Item item) {
        if (!isSessionValid(session)) {
            return "redirect:/";
        }
        try {
            List<Item> itemList = itemServices.getAllItem();
            model.addAttribute("Item", itemList);
            return "staff/item-staff";
        } catch (SQLException e) {
            System.out.println("message : " + e.getMessage());
            return "redirect:/";
        }
    }
}
