package com.k0s.web;

import com.k0s.security.SecurityService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class LogoutServlet extends HttpServlet {
    private static final String USER_TOKEN = "user-token";

    private SecurityService securityService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (USER_TOKEN.equals(cookie.getName())) {
                    securityService.logout(cookie.getValue());
                }
                cookie.setValue(null);
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
        }
        resp.sendRedirect("/");
    }
}


