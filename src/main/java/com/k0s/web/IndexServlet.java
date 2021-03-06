package com.k0s.web;

import com.k0s.entity.user.Role;
import com.k0s.security.Session;
import com.k0s.service.ProductService;
import com.k0s.util.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class IndexServlet extends HttpServlet {
    private static final String HTML_PAGE = "index.html";
    private final ProductService productService;

    public IndexServlet(ProductService productService){
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
            Map<String, Object> pageVariables = new HashMap<>();

            Session session = (Session) req.getAttribute("session");

            pageVariables.put("role", session == null ? Role.GUEST : session.getUser().getRole());
            pageVariables.put("products", productService.getAll());

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(PageGenerator.getInstance().getPage(HTML_PAGE, pageVariables));

        } catch (Exception e) {
            log.error("Crash index page", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}