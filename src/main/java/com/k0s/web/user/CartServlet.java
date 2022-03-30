package com.k0s.web.user;

import com.k0s.security.Session;
import com.k0s.util.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CartServlet extends HttpServlet {
    private static final String HTML_PAGE = "userCart.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Map<String, Object> pageVariables = new HashMap<>();
            Session session = (Session) req.getAttribute("session");

            pageVariables.put("products", session.getCart());
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(PageGenerator.getInstance().getPage(HTML_PAGE, pageVariables));
        } catch (Exception e) {
            log.info(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

        }
    }
}
