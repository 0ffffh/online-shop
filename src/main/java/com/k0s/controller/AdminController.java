package com.k0s.controller;

import com.k0s.entity.Product;
import com.k0s.service.ProductService;
import com.k0s.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;



@Controller
public class AdminController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;



    @GetMapping("/admin/product/add")
    public String addProductAdmin(ModelMap model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("products", productService.getAll());
        return "addProduct";
    }

    @PostMapping("/admin/product/add")
    public String addProductAdmin(@RequestParam String name,
                                  @RequestParam Double price,
                                  @RequestParam String description) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCreationDate(LocalDateTime.now());
        product.setDescription(description);
        productService.add(product);

        return "redirect:/admin/product/add";

    }

    @GetMapping(value = "/admin/product/edit", params = "id")
    public String editProductAdmin(@RequestParam Long id, ModelMap model, Principal principal) {
        productService.get(id).ifPresent(product -> {
            model.addAttribute("id", product.getId());
            model.addAttribute("name", product.getName());
            model.addAttribute("price", product.getPrice());
            model.addAttribute("creation_date", product.getCreationDate());
            model.addAttribute("description", product.getDescription());
            model.addAttribute("username", principal.getName());

        });

        return "editProduct";
    }

    @PostMapping("/admin/product/edit")
    public String editProductAdmin(@RequestParam Long id,
                                   @RequestParam String name,
                                   @RequestParam Double price,
                                   @RequestParam String description,
                                   ModelMap model) {

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setCreationDate(LocalDateTime.now());
        product.setDescription(description);
        productService.update(product);
        model.addAttribute("id", id);

        return "redirect:/admin/product/edit";
    }

    @GetMapping("/admin/product/delete")
    public String deleteProductAdmin(@RequestParam Long id) {

        productService.remove(id);

        return "redirect:/";
    }

    
    @GetMapping(value = "/admin/users")
    public String editUsers(ModelMap model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }

}
