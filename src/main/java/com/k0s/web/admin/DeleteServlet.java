package com.k0s.web.admin;

import com.k0s.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteServlet extends HttpServlet {
    private final ProductService productService;

    public DeleteServlet(ProductService productService) {
        this.productService = productService;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            productService.remove(Integer.parseInt(req.getParameter("id")));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());

        }
        resp.sendRedirect("/");
    }
}
