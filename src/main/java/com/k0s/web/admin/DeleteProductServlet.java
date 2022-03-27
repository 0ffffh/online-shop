package com.k0s.web.admin;

import com.k0s.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DeleteProductServlet extends HttpServlet {
    private final ProductService productService;

    public DeleteProductServlet(ProductService productService) {
        this.productService = productService;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            productService.remove(Integer.parseInt(req.getParameter("id")));
        } catch (Exception e) {
            log.error("Delete product ",e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

        }
        resp.sendRedirect("/");
    }
}
