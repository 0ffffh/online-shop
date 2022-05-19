package com.k0s.web;

import com.k0s.security.user.Role;
import com.k0s.security.Session;
import com.k0s.service.ProductService;
import com.k0s.web.util.PageGenerator;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class IndexServlet extends HttpServlet {
    private static final String HTML_PAGE = "index.html";

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        productService = (ProductService) servletContext.getAttribute("productService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
            Map<String, Object> pageVariables = new HashMap<>();
            Optional<Session> session = Optional.ofNullable((Session)req.getAttribute("session"));
            if(session.isPresent()){
                pageVariables.put("role", session.get().getUser().getRole());
            } else {
                pageVariables.put("role", Role.GUEST);
            }

            pageVariables.put("products", productService.getAll());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(PageGenerator.getInstance().getPage(HTML_PAGE, pageVariables));

        } catch (Exception e) {
            log.error("Crash index page", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}