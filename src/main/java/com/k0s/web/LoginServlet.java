package com.k0s.web;

import com.k0s.service.SecurityService;
import com.k0s.util.PageGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
public class LoginServlet extends HttpServlet {
    private final static String LOGIN_HTML_PAGE = "login.html";
    private final SecurityService securityService;

    public LoginServlet(SecurityService securityService) {
        this.securityService = securityService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage(LOGIN_HTML_PAGE, null));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("name");
        String password = req.getParameter("password");

        String token = securityService.login(username, password);
        if (token != null) {
            log.info("User <{}> authorized, token = {}", username, token);
            Cookie cookie = new Cookie("user-token", token);
            cookie.setMaxAge(securityService.getSessionMaxAge());
            resp.addCookie(cookie);
            resp.sendRedirect("/");
        } else {
            log.info("User <{}> not authorized", username);
            resp.sendRedirect("/login");
        }
    }
}

