package com.k0s.web;

import com.k0s.service.SecurityService;
import com.k0s.util.PageGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final SecurityService securityService;

    public LoginServlet(SecurityService securityService) {
        this.securityService = securityService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("login.html", null));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("name");
        String password = req.getParameter("password");

        try {
            String token = securityService.login(username, password);
//            if(token != null){
            Cookie cookie = new Cookie("user-token", token);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(10 * 60);
            System.out.println("COOKIE MAX AGE = " + cookie.getMaxAge());
            resp.addCookie(cookie);
            resp.sendRedirect("/");
//            } else {
//                System.out.println("##LOGIN ERROR ");
//                doGet(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("##LOGIN ERROR " + e);
            doGet(req, resp);
        }
    }
}
//    } catch (Exception e) {
//            System.out.println("##LOGIN ERROR " + e);
//            doGet(req, resp);
//        }
//    }
//}
