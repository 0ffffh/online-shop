package com.k0s.web.user;

import com.k0s.security.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DeleteProductFromCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Session session = (Session) req.getAttribute("session");
        session.getCart().remove(Integer.parseInt(req.getParameter("id")));

        resp.sendRedirect("/user/cart");
    }

}
