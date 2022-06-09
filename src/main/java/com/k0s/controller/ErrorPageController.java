package com.k0s.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorPageController {

    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
    }


    @GetMapping("/403")
    public String forbidden(ModelMap model) {
        model.addAttribute("status", HttpStatus.FORBIDDEN.value());
        model.addAttribute("message", HttpStatus.FORBIDDEN.getReasonPhrase());
        return "error";
    }

    @GetMapping("/404")
    public String notFound(ModelMap model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("message", HttpStatus.NOT_FOUND.getReasonPhrase());
        return "error";
    }

    @GetMapping("/500")
    public String internalError(ModelMap model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
        model.addAttribute("message", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return "error";
    }
}
