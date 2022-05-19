package com.k0s.web.admin;

import com.k0s.service.ProductService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DeleteProductServlet extends HttpServlet {
    private ProductService productService;
    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        productService = (ProductService) servletContext.getAttribute("productService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            productService.remove(Integer.parseInt(req.getParameter("id")));
        } catch (Exception e) {
            log.info("Delete product error", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
        resp.sendRedirect(req.getHeader("referer"));
    }
}
