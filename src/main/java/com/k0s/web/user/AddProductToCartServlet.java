package com.k0s.web.user;

import com.k0s.security.Session;
import com.k0s.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class AddProductToCartServlet extends HttpServlet {
    private final ProductService productService;

    public AddProductToCartServlet(ProductService productService){
        this.productService = productService;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Session session = (Session) req.getAttribute("session");
            session.addToCart(productService.get(Long.parseLong(req.getParameter("id"))));
        } catch (Exception e) {
            log.info(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

        }
        resp.sendRedirect("/");
    }

}
