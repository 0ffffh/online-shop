package com.k0s.controller;

import com.k0s.security.Session;
import com.k0s.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class UserController {
    @Autowired
    private ProductService productService;


    @GetMapping("/user/cart")
    public String userCart(ModelMap model, @RequestAttribute(value = "session", required = false) Session userSession) {

        Optional<Session> session = Optional.ofNullable(userSession);
        session.ifPresent(value -> model.addAttribute("products", value.getCart()));
        return "userCart";
    }

    @GetMapping("/user/product/add")
    public String addProductToUserCart(@RequestParam("id") Long productId, @RequestAttribute(value = "session", required = false) Session userSession) {

        Optional<Session> session = Optional.ofNullable(userSession);
        session.ifPresent(value -> productService.addToCart(value.getCart(), productId));
        return "redirect:/";

    }


    @GetMapping("/user/product/delete")
    public String deleteProductFromUserCart(@RequestParam("id") Integer index, @RequestAttribute(value = "session", required = false) Session userSession) {

        Optional<Session> session = Optional.ofNullable(userSession);
        session.ifPresent(value -> productService.removeFromCart(value.getCart(), index));
        return "redirect:/user/cart";
    }

    @GetMapping("/user/cart/clear")
    public String clearUserCart(@RequestAttribute(value = "session", required = false) Session userSession) {

        Optional<Session> session = Optional.ofNullable(userSession);
        session.ifPresent(value -> productService.clearCart(value.getCart()));
        return "redirect:/user/cart";
    }


}
