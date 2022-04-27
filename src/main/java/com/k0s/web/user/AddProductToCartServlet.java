package com.k0s.web.user;

import com.k0s.security.Session;
import com.k0s.service.ProductService;
import com.k0s.service.ServiceLocator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class AddProductToCartServlet extends HttpServlet {
    private final ProductService productService = ServiceLocator.getService(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Session session = (Session) req.getAttribute("session");
            long productId = Long.parseLong(req.getParameter("id"));
            productService.addToCart(session.getCart(), productId);
        } catch (Exception e) {
            log.info(e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.sendRedirect(req.getHeader("referer"));
    }

}
