package com.k0s.controller;

import com.k0s.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String getAll(ModelMap model) {

        setAuthentication(model);
        model.addAttribute("products", productService.getAll());

        return "index";
    }



    @GetMapping("/search")
    public String search(@RequestParam("search") String value, ModelMap model) {

        setAuthentication(model);
        model.addAttribute("products", productService.search(value));

        return "index";
    }


    private void setAuthentication(ModelMap model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        auth.getAuthorities()
                .forEach(role -> model.addAttribute(role.getAuthority(), role.getAuthority()));
        model.addAttribute("username", auth.getName());
    }
}
