package com.k0s.controller;

import com.k0s.entity.Product;
import com.k0s.security.user.Role;
import com.k0s.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;


@Controller
public class AdminController {

    @Autowired
    private ProductService productService;


    @GetMapping("/admin/product/add")
    public String addProductAdmin(ModelMap model) {
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
    public String editProductAdmin(@RequestParam Long id, ModelMap model) {

        Optional<Product> optionalProduct = Optional.ofNullable(productService.get(id));
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            model.addAttribute("id", product.getId());
            model.addAttribute("name", product.getName());
            model.addAttribute("price", product.getPrice());
            model.addAttribute("creation_date", product.getCreationDate());
            model.addAttribute("description", product.getDescription());

        }
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

    @PostMapping("/admin/product/delete")
    public String deleteProductAdmin(@RequestParam Long id) {

        productService.remove(id);

        return "redirect:/admin";
    }

}
