package com.k0s.web.filter;

import com.k0s.entity.user.Role;
import com.k0s.security.Session;
import com.k0s.service.SecurityService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AuthFilter implements Filter {
    private static final String USER_TOKEN = "user-token";
    private static final String SESSION_ATT = "session";
    private static final List<String> SKIP_AUTHORIZATION_PATH_LIST = new ArrayList<>();


    private final SecurityService securityService;

    public AuthFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SKIP_AUTHORIZATION_PATH_LIST.addAll(Arrays.asList(securityService.getProperties().getProperty("security.skipPath").split(",")));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String requestPath = req.getServletPath();

        if (SKIP_AUTHORIZATION_PATH_LIST.contains(requestPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Session session = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (USER_TOKEN.equals(cookie.getName())) {
                    session = securityService.getSession(cookie.getValue());
                    break;
                }
            }
        }
        req.setAttribute(SESSION_ATT, session);

        for (Role role : Role.values()) {
            if (requestPath.contains(role.toString())) {
                if (isAllow(session, role)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    private boolean isAllow(Session session, Role role) {
        if (session == null) {
            return false;
        }
        return role.equals(session.getRole());
    }

    @Override
    public void destroy() {
    }
}
