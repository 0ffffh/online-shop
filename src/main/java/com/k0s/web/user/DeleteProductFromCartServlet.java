package com.k0s.web.user;

import com.k0s.security.Session;
import com.k0s.service.ProductService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class DeleteProductFromCartServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        productService = (ProductService) servletContext.getAttribute("productService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Session> session = Optional.ofNullable((Session)req.getAttribute("session"));

        int index = Integer.parseInt(req.getParameter("id"));
        session.ifPresent( value -> productService.removeFromCart(value.getCart(), index));

        resp.sendRedirect(req.getHeader("referer"));

    }

}
