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
import java.util.Optional;

@Slf4j
public class AddProductToCartServlet extends HttpServlet {
    private final ProductService productService = ServiceLocator.getService(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<Session> session = Optional.ofNullable((Session)req.getAttribute("session"));
            long productId = Long.parseLong(req.getParameter("id"));
            session.ifPresent(value -> productService.addToCart(value.getCart(), productId));
        } catch (Exception e) {
            log.info(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        resp.sendRedirect(req.getHeader("referer"));
    }

}
