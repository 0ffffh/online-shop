package com.k0s.controller;

import com.k0s.security.SecurityService;
import com.k0s.security.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;


@Controller
@Slf4j
public class AuthController {
    @Autowired
    private SecurityService securityService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("name") String username,
                            @RequestParam("password") String password,
                            HttpServletResponse response) {


        Optional<Session> session = Optional.ofNullable(securityService.login(username, password));

        if (session.isPresent()) {
            log.info("User <{}> authorized, token = {}", username, session.get().getToken());
            Cookie cookie = new Cookie("user-token", session.get().getToken());

            int expiry = (int) (session.get().getExpireDate().atZone(ZoneId.systemDefault()).toEpochSecond()
                    - LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond());


            cookie.setMaxAge(expiry);
            response.addCookie(cookie);

            return "redirect:/";
        } else {
            log.info("User <{}> not authorized", username);
            return "redirect:/login";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user-token".equals(cookie.getName())) {
                    securityService.logout(cookie.getValue());
                }
                cookie.setValue(null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        return "redirect:/login";
    }


}
