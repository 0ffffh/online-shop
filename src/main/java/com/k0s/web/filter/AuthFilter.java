package com.k0s.web.filter;

import com.k0s.entity.user.Role;
import com.k0s.security.Session;
import com.k0s.service.SecurityService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class AuthFilter implements Filter {


    private final SecurityService securityService;

    public AuthFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthFilter init!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String requestUri = req.getRequestURI();
        String requestPath = req.getServletPath();
        System.out.println("FILTER REQ PATH = " + requestPath);
        System.out.println("FILTER REQ URI = " + requestUri);

        if (skipAutorization(requestPath)) {
            System.out.println("AUTH SKIP");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        System.out.println("=====AFTER SKIP======");
        Session session = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            System.out.println("CHECK COOKIE");
            for (Cookie cookie : cookies) {
                if ("user-token".equals(cookie.getName())) {
                    session = securityService.getSession(cookie.getValue());
                    break;
                }
            }
        }
        req.setAttribute("session", session);

        try {
            if (req.getServletPath().startsWith("/admin") && session.getUser().getRole().equals(Role.ADMIN)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            } else if (req.getServletPath().startsWith("/user") && session.getUser().getRole().equals(Role.USER)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Not authorize");
        }

        filterChain.doFilter(servletRequest, servletResponse);

//        resp.sendRedirect("/login");
    }

    private boolean skipAutorization(String requestUri) {
        if ("/login".equals(requestUri)) {
            return true;
        }
        if ("/resources".equals(requestUri)) {
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
