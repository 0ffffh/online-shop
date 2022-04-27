package com.k0s.web;

import com.k0s.security.user.Role;
import com.k0s.security.Session;
import com.k0s.service.ProductService;
import com.k0s.service.ServiceLocator;
import com.k0s.web.util.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class SearchServlet extends HttpServlet {
    private final ProductService productService = ServiceLocator.getService(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
            Map<String, Object> pageVariables = new HashMap<>();

            Session session = (Session) req.getAttribute("session");
            pageVariables.put("role", session == null ? Role.GUEST : session.getUser().getRole());
            pageVariables.put("products", productService.search(req.getParameter("search")));

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(PageGenerator.getInstance().getPage("index.html", pageVariables));
        }   catch (Exception e) {
            log.info("Search fail " , e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
