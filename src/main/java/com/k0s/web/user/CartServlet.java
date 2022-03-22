package com.k0s.web.user;

import com.k0s.entity.user.Role;
import com.k0s.security.Session;
import com.k0s.util.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            Map<String, Object> pageVariables = new HashMap<>();
            Session session = (Session) req.getAttribute("session");

//            pageVariables.put("isLogin", true);
            pageVariables.put("role", session.getRole());
            pageVariables.put("products", session.getCart());

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(PageGenerator.getInstance().getPage("userCart.html", pageVariables));
    }
}
