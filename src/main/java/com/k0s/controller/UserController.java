package com.k0s.controller;

import com.k0s.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;


@Controller
public class UserController {


    @Autowired
    private CartService cartService;

    @GetMapping("/user/cart")
    public String userCart(Principal principal, ModelMap model) {
        model.addAttribute("username",principal.getName());
        model.addAttribute("products", cartService.getUserCart(principal.getName()));

        return "userCart";
    }


    @GetMapping("/user/product/add")
    public String addProductToUserCart(@RequestParam("id") Long productId, Principal principal) {

        cartService.addToCart(principal.getName(), productId);
        return "redirect:/";

    }


    @GetMapping("/user/product/delete")
    public String deleteProductFromUserCart(@RequestParam("id") Long productId, Principal principal) {

        cartService.removeProduct(principal.getName(), productId);

        return "redirect:/user/cart";
    }

    @GetMapping("/user/cart/clear")
    public String clearUserCart(Principal principal) {

        cartService.clearCart(principal.getName());

        return "redirect:/user/cart";
    }


}
