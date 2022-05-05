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
public class ClearProductCartServlet extends HttpServlet {
    private final ProductService productService = ServiceLocator.getService(ProductService.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Session> session = Optional.ofNullable((Session)req.getAttribute("session"));

        session.ifPresent( value -> productService.clearCart(value.getCart()));

        resp.sendRedirect(req.getHeader("referer"));

    }

}
