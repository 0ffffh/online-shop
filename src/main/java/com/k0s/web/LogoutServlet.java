package com.k0s.web;

import com.k0s.security.Session;
import com.k0s.service.SecurityService;
import com.k0s.service.ServiceLocator;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
public class LogoutServlet extends HttpServlet {
    private static final String USER_TOKEN = "user-token";
    private final SecurityService securityService = ServiceLocator.getService(SecurityService.class);

//    private final SecurityService securityService;
//
//    public LogoutServlet(SecurityService securityService) {
//        this.securityService = securityService;
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (USER_TOKEN.equals(cookie.getName())) {
                    Session session = securityService.getSession(cookie.getValue());
                    if (session != null){
                        securityService.logout(cookie.getValue());
                        log.info("User <{}> logout", session.getUser().getName());
                    }
                }
                cookie.setValue(null);
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
        }
        resp.sendRedirect("/");
    }
}


