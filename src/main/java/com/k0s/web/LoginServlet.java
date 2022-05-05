package com.k0s.web;

import com.k0s.security.SecurityService;
import com.k0s.security.Session;
import com.k0s.service.ServiceLocator;
import com.k0s.web.util.PageGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
public class LoginServlet extends HttpServlet {
    private final static String LOGIN_HTML_PAGE = "login.html";
    private final SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage(LOGIN_HTML_PAGE, null));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("name");
        String password = req.getParameter("password");

        Optional<Session> session = Optional.ofNullable(securityService.login(username, password));

        if(session.isPresent()){

            log.info("User <{}> authorized, token = {}", username, session.get().getToken());
            Cookie cookie = new Cookie("user-token", session.get().getToken());

            int expiry = (int) (session.get().getExpireDate().atZone(ZoneId.systemDefault()).toEpochSecond()
                    - LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond());

            cookie.setMaxAge(expiry);
            resp.addCookie(cookie);
            resp.sendRedirect("/");

        } else {
            log.info("User <{}> not authorized", username);
            resp.sendRedirect(req.getHeader("referer"));
        }
    }
}

