package com.k0s.controller;

import com.k0s.security.Session;
import com.k0s.security.user.Role;
import com.k0s.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String getAll(@RequestAttribute(value = "session", required = false) Session userSession,
                         ModelMap model) {

        setSession(model, userSession);
        model.addAttribute("products", productService.getAll());
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestAttribute(value = "session", required = false) Session userSession,
                         @RequestParam("search") String value, ModelMap model) {

        setSession(model, userSession);
        model.addAttribute("products", productService.search(value));

        return "index";
    }

    private void setSession(ModelMap model, Session userSession) {

        Optional<Session> session = Optional.ofNullable(userSession);
        if (session.isPresent()) {
            model.addAttribute("role", session.get().getUser().getRole());
        } else {
            model.addAttribute("role", Role.GUEST);
        }
    }

}
