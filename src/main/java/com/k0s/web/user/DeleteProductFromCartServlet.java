package com.k0s.web.user;

import com.k0s.security.Session;
import com.k0s.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteProductFromCartServlet extends HttpServlet {
    private final ProductService productService;

    public DeleteProductFromCartServlet(ProductService productService){
        this.productService = productService;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Session session = (Session) req.getAttribute("session");
            session.getCart().remove(Integer.parseInt(req.getParameter("id")));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());

        }
        resp.sendRedirect("/user/cart");
    }

}
